package me.dkim19375.tag.view

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.shape.Circle
import me.dkim19375.tag.main
import me.dkim19375.tag.util.VIEW_TITLE
import me.dkim19375.tag.util.applyBackgroundSettings
import me.dkim19375.tag.util.setOnPress
import me.dkim19375.tag.view.skin.SkinsView1
import tornadofx.View

class StartView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val playBotButton: Button by fxid()
    private val skinsButton: Button by fxid()
    private val skinCircle: Circle by fxid()
    private val coinsLabel: Label by fxid()
    private val profileButton: Button by fxid()
    private val highscoreLabel: Label by fxid()

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
        updateCoinsLabel()
        updateSelectedCircle()
        updateProfileButton()
        updateHighscoreLabel()
    }

    fun updateHighscoreLabel() {
        highscoreLabel.text = "Highscore: ${main.profile.highscore}"
    }

    fun updateSelectedCircle() {
        skinCircle.fill = main.selectedSkin.image()
    }

    fun updateCoinsLabel() {
        coinsLabel.text = "Coins: ${main.coins}"
    }

    fun updateProfileButton() {
        profileButton.text = "Profile: ${main.profile.name}"
    }
}