package me.dkim19375.tag.multiplayer

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.DefaultClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import javafx.geometry.Point2D
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.dkim19375.tag.packet.Packet
import me.dkim19375.tag.packet.`in`.GameStartPacketIn
import me.dkim19375.tag.packet.`in`.GameStopPacketIn
import me.dkim19375.tag.packet.`in`.InfoPacketIn
import me.dkim19375.tag.packet.`in`.MovePacketIn
import me.dkim19375.tag.packet.`in`.SpeedChangePacketIn
import me.dkim19375.tag.packet.out.ConnectPacketOut
import me.dkim19375.tag.util.awaitUntilNonnull
import me.dkim19375.tag.util.sendDebugPacketMsg
import java.net.ConnectException

@Suppress("MemberVisibilityCanBePrivate")
class ClientManager {
    var host = "127.0.0.1"
    var port = 25575
    var username = "Client"
    val client: HttpClient = HttpClient(CIO) {
        install(WebSockets)
    }
    var profile: Profile? = null
    var otherProfile: Profile? = null
    var newCoords: Point2D? = null
    var speed: Double = 0.7
    var lives: Int = 5
    var session: WebSocketSession? = null

    // val coroutine = CoroutineScope(Dispatchers.Default)
    val coroutine: CoroutineScope
        get() = CoroutineScope(Dispatchers.IO)

    fun join(
        username: String,
        host: String = this.host,
        port: Int = this.port,
        success: suspend () -> Unit = {},
        failure: suspend (ConnectException) -> Unit = {}
    ) {
        val coroutine = coroutine
        this.username = username
        this.host = host
        this.port = port
        coroutine.launch {
            try {
                println("is active: ${coroutine.isActive}")
                println("is active #2: ${coroutine.isActive}")
                client.webSocket(method = HttpMethod.Get, host = host, port = port, path = "/tag") {
                    println("test 1, is active: ${coroutine.isActive}, other: $isActive")
                    coroutine.run {
                        println("success")
                        success()
                        runSession(this@webSocket)
                    }
                }
                println("client stopped")
            } catch (e: ConnectException) {
                failure(e)
            }
        }
    }

    private suspend fun runSession(session: DefaultClientWebSocketSession) = coroutineScope {
        this@ClientManager.session = session
        println("sent connect packet")
        handlePacket(ConnectPacketOut(username))
        launch {
            while (session.isActive) {
                val point = awaitUntilNonnull { newCoords }
                newCoords = null
                if (!(session.isActive)) {
                    break
                }
                session.send("move ${point.x}|${point.y}")
                delay(10L)
            }
        }
        println("test 2")
        launch frames@{
            println("listening for frames")
            for (frame in session.incoming) {
                frame as? Frame.Text ?: continue
                val text = frame.readText()
                println("got frame - $text")
                onPacketReceiving(text)
                if (text == "quit") {
                    return@frames
                }
            }
            println("stopped listening for frames")
        }
        println("test 3")
    }

    suspend fun onPacketReceiving(text: String) = coroutineScope {
        when {
            text.startsWith("info") -> handlePacket(InfoPacketIn(), text)
            text == "quit" -> otherProfile = null
            text == "start" -> handlePacket(GameStartPacketIn(), text)
            text == "stop" -> handlePacket(GameStopPacketIn(), text)
            text.startsWith("move") -> handlePacket(MovePacketIn(text), text)
            text.startsWith("speed") -> handlePacket(SpeedChangePacketIn(), text)
        }
    }

    suspend fun handlePacket(packet: Packet, text: String? = null) {
        kotlin.runCatching {
            packet.execute(
                socket = session ?: throw IllegalStateException("Session not started!"),
                text = text,
                manager = this
            )
        }.exceptionOrNull()?.printStackTrace()
        packet.sendDebugPacketMsg(text)
    }
}