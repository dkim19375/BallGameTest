package me.dkim19375.tag.packet.out

import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.send
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet

@Suppress("MemberVisibilityCanBePrivate")
class ConnectPacketOut(val username: String) : Packet {
    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ClientManager
    ): Pair<Profile?, Profile?> {
        socket.send("connect $username")
        return Pair(manager.profile, manager.otherProfile)
    }

    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ServerManager
    ): Pair<Profile?, Profile?> {
        throw IllegalStateException("Cannot execute from ServerManager!")
    }
}