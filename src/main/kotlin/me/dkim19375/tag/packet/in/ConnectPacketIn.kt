package me.dkim19375.tag.packet.`in`

import io.ktor.http.cio.websocket.*
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet

@Suppress("MemberVisibilityCanBePrivate")
class ConnectPacketIn(val enemy: Boolean, val username: String) : Packet {
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
        text ?: throw IllegalArgumentException("text cannot be null!")
        val username = text.removePrefix("connect ")
        val profile = Profile.getProfile(username, !enemy, false)
        manager.otherProfile = profile
        socket.send(Frame.Text("info ${this.username}|$enemy"))
        return Pair(manager.profile, profile)
    }
}