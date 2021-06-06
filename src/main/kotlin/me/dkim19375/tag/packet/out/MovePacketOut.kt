package me.dkim19375.tag.packet.out

import io.ktor.http.cio.websocket.*
import javafx.geometry.Point2D
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet

@Suppress("MemberVisibilityCanBePrivate")
class MovePacketOut(val point: Point2D) : Packet {
    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ClientManager
    ): Pair<Profile?, Profile?> {
        socket.send(Frame.Text("move ${point.x}|${point.y}"))
        return Pair(manager.profile, manager.otherProfile)
    }

    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ServerManager
    ): Pair<Profile?, Profile?> {
        socket.send(Frame.Text("move ${point.x}|${point.y}"))
        return Pair(manager.profile, manager.otherProfile)
    }
}