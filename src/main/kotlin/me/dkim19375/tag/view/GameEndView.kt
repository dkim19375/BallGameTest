package me.dkim19375.tag.view

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import me.dkim19375.tag.util.changeRoot
import tornadofx.View

class GameEndView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val score: Label by fxid()
    private val playAgainButton: Button by fxid()

    init {
        main.gameEndView = this
        score.text = "Score: ${main.score}"
        playAgainButton.setOnAction {
            changeRoot<GameView>()
            main.gameView.startWithPaneParam(main.gameView.root)
        }
    }
}