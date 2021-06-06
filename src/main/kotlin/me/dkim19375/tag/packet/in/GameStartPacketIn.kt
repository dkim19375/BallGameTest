package me.dkim19375.tag.packet.`in`

import io.ktor.http.cio.websocket.*
import me.dkim19375.tag.main
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet
import me.dkim19375.tag.view.GameView

class GameStartPacketIn : Packet {
    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ClientManager
    ): Pair<Profile?, Profile?> {
        main.gameView.run {
            replaceWith<GameView>()
            startWithPaneParam(main.gameView.root)
        }
        return Pair(manager.profile, manager.otherProfile)
    }

    override suspend fun execute(
        socket: WebSocketSession,
        text: String?,
        manager: ServerManager
    ): Pair<Profile?, Profile?> {
        main.gameView.run {
            replaceWith<GameView>()
            startWithPaneParam(main.gameView.root)
        }
        return Pair(manager.profile, manager.otherProfile)
    }
}