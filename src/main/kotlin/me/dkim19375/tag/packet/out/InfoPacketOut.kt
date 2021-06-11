package me.dkim19375.tag.packet.out

import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.send
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet

class InfoPacketOut : Packet {
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
        val otherProfile = manager.otherProfile ?: throw IllegalStateException("otherProfile must not be null!")
        socket.send("info ${manager.profile.username}|${otherProfile.isEnemy}")
        return Pair(manager.profile, manager.otherProfile)
    }
}