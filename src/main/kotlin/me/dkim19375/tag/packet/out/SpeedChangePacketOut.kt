package me.dkim19375.tag.packet.out

import io.ktor.http.cio.websocket.*
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet

@Suppress("MemberVisibilityCanBePrivate")
class SpeedChangePacketOut(val speed: Double) : Packet {
    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ClientManager
    ): Pair<Profile?, Profile?> {
        throw IllegalStateException("Cannot execute from ClientManager!")
    }

    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ServerManager
    ): Pair<Profile?, Profile?> {
        socket.send(Frame.Text("speed $speed"))
        return Pair(manager.profile, manager.otherProfile)
    }
}