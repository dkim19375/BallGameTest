package me.dkim19375.tag.view

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.tag.SCOPE
import me.dkim19375.tag.main
import me.dkim19375.tag.util.*
import tornadofx.View
import tornadofx.circle
import tornadofx.hbox
import tornadofx.hide
import tornadofx.keyboard
import tornadofx.label
import tornadofx.pane
import tornadofx.show
import kotlin.system.measureTimeMillis

private const val TPS = 500.0
private val tickDiff: Double
    get() = 1000.0 / TPS

@Suppress("MemberVisibilityCanBePrivate")
class GameView : View("Tag Game") {
    override val root: Pane = pane { start() }
    lateinit var user: Circle
        private set
    lateinit var enemy: Circle
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
    var active = false
    var gameStarted = false
    var pressed = mutableSetOf<KeyType>()
    var speed = 0.7
    var lives = 5
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
        enemyFrozen = false
        if (this::gameOverLabel.isInitialized) {
            gameOverLabel.hide()
        }
    }

    fun startWithPaneParam(pane: Pane) = pane.start()

    fun Pane.start() {
        reset()
        active = true
        setupUserBall()
        setupEnemyBall()
        setupVariables()
        SCOPE.launch {
            var first = true
            while (true) {
                if (!active) {
                    continue
                }
                if (gameStarted && !first) {
                    speed += 0.05
                    main.score++
                }
                Platform.runLater { scoreLabel.text = "Score: ${main.score}" }
                Platform.runLater { livesLabel.text = "Lives: $lives" }
                first = false
                delay(1000L)
            }
        }
        SCOPE.launch {
            delay(3000L)
            gameStarted = true
            while (true) {
                if (!active) {
                    continue
                }
                val off = measureTimeMillis {
                    move()
                    detectHit()
                }
                if ((off > tickDiff) && ((off - tickDiff) / tickDiff >= 1.0)) {
                    System.err.println("Running ${(off - tickDiff).toInt()}ms behind (${((off - tickDiff) / tickDiff).toInt()} ticks)!")
                }
                delay((tickDiff - off).toLong())
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
            user.teleport((-200).getX(), 0.getY())
            return
        }
        user = circle(centerX = (-200).getX(), centerY = 0.getY(), radius = 50)
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) { event ->
                pressed.add(getCharFromEvent(event) ?: return@addEventHandler)
            }
            addEventHandler(KeyEvent.KEY_RELEASED) { event ->
                pressed.remove(getCharFromEvent(event) ?: return@addEventHandler)
            }
        }
    }

    private fun Pane.setupVariables() {
        var finished = false
        if (this@GameView::topHBox.isInitialized) {
            gameOverLabel.hide()
            return
        }
        topHBox = hbox {
            alignment = Pos.CENTER
            SCOPE.launch {
                await { finished }
                while (true) {
                    updateTopHBox()
                    delay(50L)
                }
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
        topHBox.label("                   ") { font = Font.font("System", 70.0) }
        livesLabel = topHBox.label("Lives: $lives") { font = Font.font("System", 50.0) }
        mainLabel = label("Tag") { font = Font.font("System", 70.0) }
        finished = true
    }

    private fun HBox.updateTopHBox() {
        mainLabel.teleport(centerX - (mainLabel.width / 2), 50.0)
        teleport(centerX - (width / 2), 50.0)
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

    private fun move() {
        val newSet = try {
            pressed.toSet()
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
            return
        } catch (e: java.util.ConcurrentModificationException) {
            e.printStackTrace()
            return
        }
        newSet.forEach { type ->
            user.teleport(
                user.getPoint(type, speed).setBounds(
                    maxX = windowX - user.radius * 1.5,
                    maxY = windowY - user.radius * 1.5,
                    minX = -(user.radius * 0.5),
                    minY = -(user.radius * 0.5)
                )
            )
        }
        if (!enemyFrozen) {
            val enemyLoc = enemy.getLocation()
            enemy.teleport(enemyLoc.getDirectionPoint(speed * 0.97, enemyLoc.getAngle(user.getLocation())))
        }
    }

    private fun detectHit() {
        if (enemyFrozen) {
            return
        }
        if (!user.isTouching(enemy)) {
            return
        }
        if (lives <= 1) {
            active = false
            SCOPE.launch {
                gameOverLabel.show()
                delay(3000L)
                Platform.runLater { replaceWith<GameEndView>() }
            }
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
}