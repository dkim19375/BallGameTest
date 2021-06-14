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
import me.dkim19375.tag.util.changeRoot
import tornadofx.View

@Suppress("MemberVisibilityCanBePrivate")
class StartView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    val playBotButton: Button by fxid()
    val skinsButton: Button by fxid()
    val skinCircle: Circle by fxid()
    val coinsLabel: Label by fxid()

    init {
        main.startView = this
        start()
    }

    fun start() {
        playBotButton.setOnAction {
            changeRoot<GameView>()
            main.gameView.startWithPaneParam(main.gameView.root)
        }
        skinsButton.setOnAction {
            changeRoot<SkinsView>()
            main.skinsView.start()
        }
        updateCoinsLabel()
        updateSelectedCircle()
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
}