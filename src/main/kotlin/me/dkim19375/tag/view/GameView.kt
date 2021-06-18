package me.dkim19375.tag.view

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import me.dkim19375.tag.main
import me.dkim19375.tag.manager.GameManager
import me.dkim19375.tag.util.*
import tornadofx.*

@Suppress("MemberVisibilityCanBePrivate")
class GameView : View(VIEW_TITLE) {
    override val root: Pane = pane {
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) { event -> main.gameManager.onKeyPressed(event.code) }
            addEventHandler(KeyEvent.KEY_RELEASED) { event -> main.gameManager.onKeyReleased(event.code) }
        }
        applyBackgroundSettings()
    }
    private val gameManager: GameManager = main.gameManager
    lateinit var user: Circle
        private set
    lateinit var enemy: Circle
        private set
    lateinit var coin: Circle
        private set
    lateinit var topVBox: VBox
        private set
    lateinit var topHBox: HBox
        private set
    lateinit var scoreLabel: Label
        private set
    lateinit var livesLabel: Label
        private set
    lateinit var mainLabel: Label
        private set
    lateinit var gameOverLabel: Label
        private set
    lateinit var tpsLabel: Label
        private set
    lateinit var coinsLabel: Label
        private set

    init {
        main.gameView = this
    }

    fun reset() {
        if (this::gameOverLabel.isInitialized) {
            gameOverLabel.hide()
        }
    }

    fun updateLabels() = runSync {
        scoreLabel.text = "Score: ${main.score}"
        livesLabel.text = "Lives: ${gameManager.lives}"
        tpsLabel.text = "TPS: ${gameManager.latestTPS}/${TPS.toInt()}"
        coinsLabel.text = "Coins: ${gameManager.coins}"
    }

    fun setupUserBall() {
        if (!this@GameView::user.isInitialized) {
            user = root.circle(centerX = (-200).getX(), centerY = 0.getY(), radius = 50)
        }
        user.run {
            fill = main.selectedSkin.image()
            strokeWidth = 1.0
            stroke = Color.BLACK
        }
    }

    fun setupEnemyBall() {
        if (!this@GameView::enemy.isInitialized) {
            enemy = root.circle(centerX = 200.getX(), centerY = 0.getY(), radius = 50)
        }
        enemy.run {
            fill = Color.RED
            stroke = Color.BLACK
        }
    }

    fun setupLabels(finished: () -> Unit) = root.run {
        if (this@GameView::topHBox.isInitialized) {
            gameOverLabel.hide()
            return
        }
        topVBox = vbox {
            alignment = Pos.CENTER
            tpsLabel = label("TPS: 0/0") {
                font = Font.font("System", 50.0)
                alignment = Pos.CENTER
            }
            tpsLabel.hide()
            topHBox = hbox {
                alignment = Pos.CENTER
            }
        }
        gameOverLabel = label("Game Over!") {
            font = Font.font("System", 70.0)
            alignment = Pos.CENTER
            hide()
        }
        scoreLabel = topHBox.label("Score: ${main.score}") { font = Font.font("System", 50.0) }
        topHBox.label("          ") { font = Font.font("System", 70.0) }
        livesLabel = topHBox.label("Lives: ${gameManager.lives}") { font = Font.font("System", 50.0) }
        topHBox.label("          ") { font = Font.font("System", 70.0) }
        mainLabel = topVBox.label("Tag") { font = Font.font("System", 70.0) }
        coinsLabel = topHBox.label("Coins: ${gameManager.coins}") { font = Font.font("System", 50.0) }
        finished()
    }

    fun updateTopVBox() = topVBox.run {
        alignment = Pos.CENTER
        topHBox.alignment = Pos.CENTER
        teleport(centerX - (width / 2), height / 10)
    }

    fun setupCoin() = root.run {
        if (!this@GameView::coin.isInitialized) {
            coin = circle(radius = 40)
        }
        coin.run {
            fill = ImagePattern(Image("images/coin.png"))
            stroke = Color.BLACK
            strokeWidth = 1.0
        }
    }

    fun stop() {
        gameOverLabel.show()
    }
}