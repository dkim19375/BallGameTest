package me.dkim19375.tag.packet.`in`

import io.ktor.http.cio.websocket.*
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet

class SpeedChangePacketIn : Packet {
    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ClientManager
    ): Pair<Profile?, Profile?> {
        manager.speed = (text ?: throw IllegalArgumentException("text cannot be null!")).split(" ")[1].toDouble()
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