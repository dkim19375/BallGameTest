package me.dkim19375.tag.view

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import me.dkim19375.tag.util.onMainThread
import tornadofx.View

class GameEndView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val score: Label by fxid()
    private val playAgainButton: Button by fxid()
    private val homeButton: Button by fxid()
    private val coins: Label by fxid()

    init {
        main.gameEndView = this
    }

    fun start(coinsCollected: Int) {
        if (!onMainThread()) {
            Platform.runLater {
                start(coinsCollected)
            }
            return
        }
        score.text = "Score: ${main.score}"
        playAgainButton.setOnAction {
            replaceWith<GameView>()
            main.gameView.startWithPaneParam(main.gameView.root)
        }
        homeButton.setOnAction {
            replaceWith<StartView>()
        }
        coins.text = "Coins Collected: $coinsCollected"
    }
}