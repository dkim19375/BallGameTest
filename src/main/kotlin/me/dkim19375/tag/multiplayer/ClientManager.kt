package me.dkim19375.tag.multiplayer

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import javafx.geometry.Point2D
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.dkim19375.tag.SCOPE
import me.dkim19375.tag.packet.Packet
import me.dkim19375.tag.packet.`in`.GameStartPacketIn
import me.dkim19375.tag.packet.`in`.GameStopPacketIn
import me.dkim19375.tag.packet.`in`.MovePacketIn
import me.dkim19375.tag.packet.`in`.SpeedChangePacketIn
import me.dkim19375.tag.packet.out.ConnectPacketOut
import me.dkim19375.tag.util.awaitUntilNonnull
import java.net.ConnectException

@Suppress("MemberVisibilityCanBePrivate")
class ClientManager {
    var host = "127.0.0.1"
    var port = 25575
    var username = "Client"
    val client: HttpClient = HttpClient {
        install(WebSockets)
    }
    var profile: Profile? = null
    var otherProfile: Profile? = null
    var newCoords: Point2D? = null
    var speed: Double = 0.7
    var lives: Int = 5
    var session: WebSocketSession? = null

    fun join(
        username: String,
        host: String = this.host,
        port: Int = this.port,
        success: () -> Unit = {},
        failure: (ConnectException) -> Unit = {}
    ) {
        this.username = username
        this.host = host
        this.port = port
        SCOPE.launch {
            try {
                client.webSocket(method = HttpMethod.Get, host = host, port = port, path = "/tag") {
                    println("test 1")
                    SCOPE.launch {
                        val session = this@webSocket
                        this@ClientManager.session = session
                        println("success")
                        success()
                        while (session.isActive) {
                            val point = awaitUntilNonnull { newCoords }
                            newCoords = null
                            if (!(session.isActive)) {
                                break
                            }
                            send(Frame.Text("move ${point.x}|${point.y}"))
                            delay(10L)
                        }
                    }
                    println("test 2")
                    SCOPE.launch frames@{
                        println("listening for frames")
                        while (true) {
                            val frame = incoming.receive()
                            frame as? Frame.Text ?: continue
                            val text = frame.readText()
                            println("got frame - $text")
                            when {
                                text.startsWith("info") -> {
                                    val args = text.removePrefix("info ").split('|')
                                    otherProfile = Profile.getProfile(
                                        username = args[0],
                                        enemy = args[1].toBooleanStrict(),
                                        isServer = true,
                                        location = Point2D(0.0, 0.0)
                                    )
                                    profile = Profile.getProfile(
                                        username = this@ClientManager.username,
                                        enemy = !args[1].toBooleanStrict(),
                                        isServer = false,
                                        location = Point2D(0.0, 0.0)
                                    )
                                }
                                text == "quit" -> {
                                    otherProfile = null
                                    return@frames
                                }
                                text == "start" -> handlePacket(GameStartPacketIn(), text)
                                text == "stop" -> handlePacket(GameStopPacketIn(), text)
                                text.startsWith("move") -> handlePacket(MovePacketIn(text), text)
                                text.startsWith("speed") -> handlePacket(SpeedChangePacketIn(), text)
                            }
                        }
                    }
                    println("test 3")
                    SCOPE.launch {
                        println("sent connect packet")
                        handlePacket(ConnectPacketOut(username))
                    }
                }
                println("client stopped")
            } catch (e: ConnectException) {
                failure(e)
            }
        }
    }

    suspend fun handlePacket(packet: Packet, text: String? = null) {
        packet.execute(session ?: throw IllegalStateException("Session not started!"), text, this)
    }

    fun handlePacketNonCoroutine(packet: Packet, text: String? = null) {
        SCOPE.launch {
            packet.execute(session ?: throw IllegalStateException("Session not started!"), text, this@ClientManager)
        }
    }
}