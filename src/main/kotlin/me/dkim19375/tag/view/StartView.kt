package me.dkim19375.tag.view

import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import me.dkim19375.tag.main
import me.dkim19375.tag.util.*
import me.dkim19375.tag.view.skin.SkinsView1
import tornadofx.*

class StartView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val playBotBox: HBox by fxid()
    private val skinsBox: HBox by fxid()
    private val profileBox: HBox by fxid()
    private val skinCircle: Circle by fxid()
    private val coinsLabel: Label by fxid()
    private val highscoreLabel: Label by fxid()
    private val playBotButton: JFXButton = kfxButton("Play", playBotBox)
    private val skinsButton: JFXButton = kfxButton("Skins", skinsBox)
    private val profileButton: JFXButton = kfxButton("Profile: ", profileBox)

    init {
        main.startView = this
        start()
    }

    private fun start() {
        root.applyBackgroundSettings()
        playBotButton.setOnPress {
            replaceWith<GameView>()
            main.gameManager.start()
        }
        skinsButton.setOnPress {
            replaceWith<SkinsView1>()
            main.skinsView1.start()
        }
        profileButton.setOnPress {
            replaceWith<ProfileView>()
            main.profileView.start()
        }
        update()
    }

    fun update() {
        if (!onMainThread()) {
            Platform.runLater {
                update()
            }
            return
        }
        root.requestFocus()
        updateCoinsLabel()
        updateSelectedCircle()
        updateProfileButton()
        updateHighscoreLabel()
    }

    fun updateHighscoreLabel() {
        highscoreLabel.text = "Highscore: ${main.profile.highscore}"
    }

    fun updateSelectedCircle() {
        skinCircle.run {
            fill = main.selectedSkin.image()
            stroke = Color.rgb(32, 50, 205)
            strokeWidth = 4.0
        }
    }

    fun updateCoinsLabel() {
        coinsLabel.text = "Coins: ${main.coins}"
    }

    fun updateProfileButton() {
        profileButton.text = "Profile: ${main.profile.name}"
    }
}