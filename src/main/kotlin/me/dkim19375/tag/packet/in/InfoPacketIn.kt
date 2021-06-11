package me.dkim19375.tag.packet.`in`

import io.ktor.http.cio.websocket.WebSocketSession
import javafx.geometry.Point2D
import me.dkim19375.tag.main
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet
import tornadofx.hide

class InfoPacketIn : Packet {
    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ClientManager
    ): Pair<Profile?, Profile?> {
        text ?: throw IllegalArgumentException("text cannot be null!")
        val args = text.removePrefix("info ").split('|')
        val username = args[0]
        val enemy = args[1].toBooleanStrict()
        manager.otherProfile = Profile.getProfile(
            username = username,
            enemy = !enemy,
            isServer = true,
            location = Point2D(0.0, 0.0)
        )
        manager.profile = Profile.getProfile(
            username = manager.username,
            enemy = enemy,
            isServer = false,
            location = Point2D(0.0, 0.0)
        )
        main.lobbyView.startButton.hide()
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