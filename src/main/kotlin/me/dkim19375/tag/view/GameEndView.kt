package me.dkim19375.tag.view

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import me.dkim19375.tag.main
import me.dkim19375.tag.util.VIEW_TITLE
import me.dkim19375.tag.util.applyBackgroundSettings
import me.dkim19375.tag.util.onMainThread
import me.dkim19375.tag.util.setOnPress
import tornadofx.View

class GameEndView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val score: Label by fxid()
    private val playAgainButton: Button by fxid()
    private val homeButton: Button by fxid()
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
        }
        coins.text = "Coins Collected: $coinsCollected"
        highscoreLabel.text = if (main.profile.highscore == main.score) {
            "NEW Highscore: ${main.score} (Old: $oldHighscore)"
        } else {
            "Highscore: ${main.profile.highscore}"
        }
    }
}