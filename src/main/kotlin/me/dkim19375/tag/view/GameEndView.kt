package me.dkim19375.tag.view

import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import me.dkim19375.tag.main
import me.dkim19375.tag.util.*
import tornadofx.*

class GameEndView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val score: Label by fxid()
    private val playAgainBox: HBox by fxid()
    private val homeBox: HBox by fxid()
    private val playAgainButton: JFXButton = kfxButton("Play Again", playAgainBox)
    private val homeButton: JFXButton = kfxButton("Go to Home", homeBox)
    private val coins: Label by fxid()
    private val highscoreLabel: Label by fxid()

    init {
        main.gameEndView = this
        root.applyBackgroundSettings()
    }

    fun start(coinsCollected: Int, oldHighscore: Int) {
        if (!onMainThread()) {
            Platform.runLater {
                start(coinsCollected, oldHighscore)
            }
            return
        }
        score.text = "Score: ${main.score}"
        playAgainButton.setOnPress {
            replaceWith<GameView>()
            main.gameManager.start()
        }
        homeButton.setOnPress {
            replaceWith<StartView>()
            main.startView.update()
        }
        coins.text = "Coins Collected: $coinsCollected"
        highscoreLabel.text = if (main.profile.highscore == main.score) {
            "NEW Highscore: ${main.score} (Old: $oldHighscore)"
        } else {
            "Highscore: ${main.profile.highscore}"
        }
    }
}