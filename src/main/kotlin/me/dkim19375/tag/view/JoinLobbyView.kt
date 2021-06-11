package me.dkim19375.tag.view

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import me.dkim19375.tag.util.changeRoot
import tornadofx.View
import tornadofx.hide
import tornadofx.show

class JoinLobbyView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val joinButton: Button by fxid()
    private val ip: TextField by fxid()
    private val error: Label by fxid()
    private var started = false

    init {
        main.joinLobbyView = this
        start()
    }

    fun start() {
        started = false
        error.hide()
        joinButton.setOnAction {
            if (started) {
                return@setOnAction
            }
            error.hide()
            started = true
            val array = ip.text.split(':')
            val host = array[0]
            val port: Int = array.getOrNull(1)?.toIntOrNull() ?: main.clientManager.port
            main.clientManager.join(
                username = "Client",
                host = host,
                port = port,
                success = {
                    Platform.runLater {
                        changeRoot<LobbyView>()
                        main.lobbyView.reset()
                    }
                }
            ) { exception ->
                Platform.runLater {
                    error.text = exception.localizedMessage
                    error.show()
                }
            }
        }
    }
}