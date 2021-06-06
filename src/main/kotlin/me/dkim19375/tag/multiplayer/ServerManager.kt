package me.dkim19375.tag.multiplayer

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import javafx.geometry.Point2D
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.dkim19375.tag.SCOPE
import me.dkim19375.tag.main
import me.dkim19375.tag.packet.Packet
import me.dkim19375.tag.packet.`in`.ConnectPacketIn
import me.dkim19375.tag.packet.`in`.GameStartPacketIn
import me.dkim19375.tag.packet.`in`.GameStopPacketIn
import me.dkim19375.tag.packet.`in`.MovePacketIn
import me.dkim19375.tag.packet.`in`.SpeedChangePacketIn
import me.dkim19375.tag.util.awaitUntilNonnull
import me.dkim19375.tag.util.getLocation
import kotlin.random.Random

@Suppress("MemberVisibilityCanBePrivate")
class ServerManager {
    var enabled = false
        private set
    var port = 25575
    var engine: ApplicationEngine? = null
    var username = "Host"
    val profile: Profile
        get() = Profile.getProfile(
            username, enemy, true, try {
                main.gameView.user.getLocation()
            } catch (_: UninitializedPropertyAccessException) {
                Point2D(0.0, 0.0)
            }
        )
    var otherProfile: Profile? = null
    var enemy = false
    var newCoords: Point2D? = null
    var session: WebSocketSession? = null

    fun start(port: Int = this.port) {
        enemy = Random.nextBoolean()
        enabled = true
        this.port = port
        SCOPE.launch {
            engine = embeddedServer(Netty, port = port) {
                install(WebSockets)
                routing {
                    webSocket("/tag") {
                        session = this
                        val ip = call.request.origin.host
                        println("got request from $ip")
                        launch {
                            val session = this@webSocket
                            while (session.isActive && enabled) {
                                val point = awaitUntilNonnull { newCoords }
                                newCoords = null
                                if (!(session.isActive && enabled)) {
                                    break
                                }
                                MovePacketIn(point).execute(session, null, this@ServerManager)
                                delay(10L)
                            }
                        }
                        launch {
                            println("listening for frames")
                            while (true) {
                                val frame = incoming.receive()
                                println("got a frame")
                                frame as? Frame.Text ?: continue
                                val text = frame.readText()
                                println("got frame - $text")
                                when {
                                    text.startsWith("connect") -> ConnectPacketIn(enemy, username)
                                    text == "quit" -> otherProfile = null
                                    text == "start" -> handlePacket(GameStartPacketIn(), text)
                                    text == "stop" -> handlePacket(GameStopPacketIn(), text)
                                    text.startsWith("move") -> handlePacket(MovePacketIn(text), text)
                                    text.startsWith("speed") -> handlePacket(SpeedChangePacketIn(), text)
                                }
                            }
                        }
                    }
                }
            }
            engine?.start()
        }
    }

    suspend fun handlePacket(packet: Packet, text: String? = null) {
        packet.execute(session ?: throw IllegalStateException("Session not started!"), text, this)
    }

    fun handlePacketNonCoroutine(packet: Packet, text: String? = null) {
        SCOPE.launch {
            packet.execute(session ?: throw IllegalStateException("Session not started!"), text, this@ServerManager)
        }
    }

    fun stop() {
        engine?.stop(1000L, 1000L)
        enabled = false
    }
}