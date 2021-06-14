package me.dkim19375.tag.view

import javafx.application.Platform
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.tag.SCOPE
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import me.dkim19375.tag.util.KeyType
import me.dkim19375.tag.util.SkinType
import me.dkim19375.tag.util.await
import me.dkim19375.tag.util.centerX
import me.dkim19375.tag.util.getAngle
import me.dkim19375.tag.util.getDirectionPoint
import me.dkim19375.tag.util.getLocation
import me.dkim19375.tag.util.getPoint
import me.dkim19375.tag.util.getX
import me.dkim19375.tag.util.getY
import me.dkim19375.tag.util.isTouching
import me.dkim19375.tag.util.onMainThread
import me.dkim19375.tag.util.setBounds
import me.dkim19375.tag.util.teleport
import me.dkim19375.tag.util.toKeyType
import me.dkim19375.tag.util.windowX
import me.dkim19375.tag.util.windowY
import tornadofx.View
import tornadofx.circle
import tornadofx.hbox
import tornadofx.hide
import tornadofx.keyboard
import tornadofx.label
import tornadofx.pane
import tornadofx.show
import tornadofx.vbox
import kotlin.math.min
import kotlin.system.measureTimeMillis

private const val TPS = 500.0
private val tickDiff: Double
    get() = 1000.0 / TPS

@Suppress("MemberVisibilityCanBePrivate")
class GameView : View(VIEW_TITLE) {
    override val root: Pane = pane {
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) { event ->
                pressed.add(getCharFromEvent(event) ?: return@addEventHandler)
            }
            addEventHandler(KeyEvent.KEY_RELEASED) { event ->
                pressed.remove(getCharFromEvent(event) ?: return@addEventHandler)
            }
        }
    }
    lateinit var user: Circle
        private set
    lateinit var enemy: Circle
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
    var active = false
    var latestTPS = TPS.toInt()
    var gameStarted = false
    var pressed = mutableSetOf<KeyType>()
    var speed = 0.7
    var lives = 5
    var coins = 0
    var enemyFrozen = false

    init {
        main.gameView = this
    }

    private fun reset() {
        active = false
        gameStarted = false
        pressed = mutableSetOf()
        speed = 0.7
        main.score = 0
        lives = 5
        coins = 0
        enemyFrozen = false
        if (this::gameOverLabel.isInitialized) {
            gameOverLabel.hide()
        }
    }

    fun startWithPaneParam(pane: Pane) = pane.start()

    fun Pane.start() {
        if (!onMainThread()) {
            Platform.runLater {
                start()
            }
            return
        }
        reset()
        setupUserBall()
        setupEnemyBall()
        setupVariables()
        active = true
        SCOPE.launch {
            var first = true
            while (true) {
                if (!active) {
                    return@launch
                }
                if (gameStarted && !first) {
                    speed += 0.05
                    main.score++
                }
                Platform.runLater {
                    scoreLabel.text = "Score: ${main.score}"
                    livesLabel.text = "Lives: $lives"
                    tpsLabel.text = "TPS: $latestTPS/${TPS.toInt()}"
                    coinsLabel.text = "Coins: $coins"
                }
                first = false
                delay(1000L)
            }
        }
        SCOPE.launch {
            delay(3000L)
            gameStarted = true
            while (true) {
                if (!active) {
                    return@launch
                }
                val off = measureTimeMillis {
                    move()
                    detectHit()
                }
                if ((off > tickDiff) && ((off - tickDiff) / tickDiff >= 10.0)) {
                    System.err.println("Running ${(off - tickDiff).toInt()}ms behind (${((off - tickDiff) / tickDiff).toInt()} ticks)!")
                }
                latestTPS = min((TPS.toInt() - ((off - tickDiff) / tickDiff).toInt()), TPS.toInt())
                delay((tickDiff - off).toLong())
            }
        }
        SCOPE.launch {
            await { gameStarted }
            while (true) {
                if (!active) {
                    return@launch
                }
                if (enemyFrozen) {
                    delay(3000L)
                    continue
                }
                coins++
                Platform.runLater {
                    coinsLabel.text = "Coins: $coins"
                }
                delay(3000L)
            }
        }
    }

    private fun getCharFromEvent(event: KeyEvent): KeyType? {
        val char = try {
            if (event.text.length > 1) {
                throw IndexOutOfBoundsException()
            }
            event.text[0]
        } catch (_: IndexOutOfBoundsException) {
            return null
        }
        return char.toKeyType()
    }

    private fun Pane.setupUserBall() {
        if (this@GameView::user.isInitialized) {
            user.fill = if (main.selectedSkin == SkinType.DEFAULT) {
                Color.BLACK
            } else {
                ImagePattern(Image(main.selectedSkin.imagePath))
            }
            user.teleport((-200).getX(), 0.getY())
            user.strokeWidth = 1.0
            user.stroke = Color.BLACK
            return
        }
        user = circle(centerX = (-200).getX(), centerY = 0.getY(), radius = 50)
        user.fill = if (main.selectedSkin == SkinType.DEFAULT) {
            Color.BLACK
        } else {
            ImagePattern(Image(main.selectedSkin.imagePath))
        }
        user.strokeWidth = 1.0
        user.stroke = Color.BLACK
    }

    private fun Pane.setupVariables() {
        var finished = false
        if (this@GameView::topHBox.isInitialized) {
            gameOverLabel.hide()
            return
        }
        topVBox = vbox {
            alignment = Pos.CENTER
            SCOPE.launch {
                await { finished }
                while (true) {
                    updateTopVBox()
                    delay(50L)
                }
            }
            tpsLabel = label("TPS: 0/0") {
                font = Font.font("System", 50.0)
                alignment = Pos.CENTER
            }
            topHBox = hbox {
                alignment = Pos.CENTER
            }
        }
        gameOverLabel = label("Game Over!") {
            font = Font.font("System", 70.0)
            alignment = Pos.CENTER
            hide()
            SCOPE.launch {
                await { finished }
                while (true) {
                    teleport(centerX - (width / 2), windowY - height - 100.0)
                    delay(50L)
                }
            }
        }
        scoreLabel = topHBox.label("Score: ${main.score}") { font = Font.font("System", 50.0) }
        topHBox.label("          ") { font = Font.font("System", 70.0) }
        livesLabel = topHBox.label("Lives: $lives") { font = Font.font("System", 50.0) }
        topHBox.label("          ") { font = Font.font("System", 70.0) }
        mainLabel = topVBox.label("Tag") { font = Font.font("System", 70.0) }
        coinsLabel = topHBox.label("Coins: $coins") { font = Font.font("System", 50.0) }
        finished = true
    }

    private fun VBox.updateTopVBox() {
        alignment = Pos.CENTER
        topHBox.alignment = Pos.CENTER
        teleport(centerX - (width / 2), height / 10)
    }

    private fun Pane.setupEnemyBall() {
        if (this@GameView::enemy.isInitialized) {
            enemy.teleport(200.getX(), 0.getY())
            return
        }
        enemy = circle(centerX = 200.getX(), centerY = 0.getY(), radius = 50) {
            fill = Color.RED
            stroke = Color.BLACK
        }
    }

    @Synchronized
    private fun move() {
        pressed.toSet().forEach { type ->
            val loc = user.getPoint(type, speed).setBounds(
                maxX = windowX - user.radius * 1.5,
                maxY = windowY - user.radius * 1.5,
                minX = -(user.radius * 0.5),
                minY = -(user.radius * 0.5)
            )
            user.teleport(loc)
        }
        if (!enemyFrozen) {
            val enemyLoc = enemy.getLocation()
            enemy.teleport(enemyLoc.getDirectionPoint(speed * 0.97, enemyLoc.getAngle(user.getLocation())))
        }
    }

    @Synchronized
    private fun detectHit() {
        if (enemyFrozen) {
            return
        }
        if (!user.isTouching(enemy)) {
            return
        }
        if (lives <= 1) {
            stop()
            return
        }
        lives--
        Platform.runLater { livesLabel.text = "Lives: $lives" }
        SCOPE.launch {
            enemyFrozen = true
            delay(3000L)
            enemyFrozen = false
        }
    }

    fun stop() {
        active = false
        main.coins += coins
        main.startView.updateCoinsLabel()
        gameOverLabel.show()
        SCOPE.launch {
            delay(3000L)
            Platform.runLater {
                replaceWith<GameEndView>()
                main.gameEndView.start(coins)
            }
        }
    }
}