package me.dkim19375.tag.view

import javafx.scene.control.Button
import javafx.scene.layout.VBox
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import tornadofx.View

@Suppress("MemberVisibilityCanBePrivate")
class StartView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    var started = false
    val playBotButton: Button by fxid()
    val playMultiButton: Button by fxid()
    val startServerButton: Button by fxid()

    init {
        start()
    }

    fun start() {
        started = false
        playBotButton.setOnAction {
            if (started) {
                return@setOnAction
            }
            started = true
            replaceWith<GameView>()
            main.gameView.startWithPaneParam(main.gameView.root)
        }
        startServerButton.setOnAction {
            if (started) {
                return@setOnAction
            }
            started = true
            replaceWith<LobbyView>()
            main.lobbyView.reset()
        }
        playMultiButton.setOnAction {
            if (started) {
                return@setOnAction
            }
            started = true
            replaceWith<JoinLobbyView>()
        }
    }
}