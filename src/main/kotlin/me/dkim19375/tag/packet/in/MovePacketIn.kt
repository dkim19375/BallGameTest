package me.dkim19375.tag.packet.`in`

import io.ktor.http.cio.websocket.*
import javafx.geometry.Point2D
import me.dkim19375.tag.main
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet
import me.dkim19375.tag.util.teleport

@Suppress("MemberVisibilityCanBePrivate")
class MovePacketIn(val point: Point2D) : Packet {
    constructor(text: String) : this(
        Point2D(
            text.removePrefix("move").split('|')[0].toDouble(),
            text.removePrefix("move").split('|')[1].toDouble()
        )
    )

    private fun get(otherProfile: Profile?): Profile? {
        if (otherProfile?.isEnemy == true) {
            main.gameView.enemy.teleport(point)
        } else {
            main.gameView.user.teleport(point)
        }
        return otherProfile?.copy(location = point)
    }

    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ClientManager
    ): Pair<Profile?, Profile?> {
        val new = get(manager.otherProfile)
        manager.otherProfile = new
        return Pair(manager.profile, new)
    }

    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ServerManager
    ): Pair<Profile?, Profile?> {
        val new = get(manager.otherProfile)
        manager.otherProfile = new
        return Pair(manager.profile, new)
    }
}