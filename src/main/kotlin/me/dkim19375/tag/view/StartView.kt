package me.dkim19375.tag.view

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import tornadofx.View

class StartView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val playBotButton: Button by fxid()
    private val skinsButton: Button by fxid()
    private val skinCircle: Circle by fxid()
    private val coinsLabel: Label by fxid()
    private val profileButton: Button by fxid()

    init {
        main.startView = this
        start()
    }

    private fun start() {
        playBotButton.setOnAction {
            replaceWith<GameView>()
            main.gameView.startWithPaneParam(main.gameView.root)
        }
        skinsButton.setOnAction {
            replaceWith<SkinsView>()
            main.skinsView.start()
        }
        profileButton.setOnAction {
            replaceWith<ProfileView>()
            main.profileView.start()
        }
        updateCoinsLabel()
        updateSelectedCircle()
        updateProfileButton()
    }

    fun updateSelectedCircle() {
        skinCircle.fill = try {
            ImagePattern(Image("images/skins/${main.selectedSkin.intValue}.png"))
        } catch (_: IllegalArgumentException) {
            Color.BLACK
        }
    }

    fun updateCoinsLabel() {
        coinsLabel.text = "Coins: ${main.coins}"
    }

    fun updateProfileButton() {
        profileButton.text = "Profile: ${main.profile.name}"
    }
}