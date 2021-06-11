package me.dkim19375.tag.packet.out

import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import me.dkim19375.tag.main
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.packet.Packet
import me.dkim19375.tag.util.changeRoot
import me.dkim19375.tag.view.GameView

class GameStartPacketOut : Packet {
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
        socket.send(Frame.Text("start"))
        changeRoot<GameView>()
        main.gameView.startWithPaneParam(main.gameView.root)
        return Pair(manager.profile, manager.otherProfile)
    }
}