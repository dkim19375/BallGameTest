package me.dkim19375.tag.view

import javafx.scene.control.Button
import javafx.scene.layout.VBox
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import me.dkim19375.tag.util.changeRoot
import tornadofx.View

@Suppress("MemberVisibilityCanBePrivate")
class StartView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    var started = false
    val playBotButton: Button by fxid()

    init {
        main.startView = this
        start()
    }

    fun start() {
        started = false
        playBotButton.setOnAction {
            if (started) {
                return@setOnAction
            }
            started = true
            changeRoot<GameView>()
            main.gameView.startWithPaneParam(main.gameView.root)
        }
    }
}