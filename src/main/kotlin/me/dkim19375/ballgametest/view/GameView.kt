package me.dkim19375.ballgametest.view

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
import me.dkim19375.ballgametest.SCOPE
import me.dkim19375.ballgametest.util.*
import tornadofx.View
import tornadofx.circle
import tornadofx.hbox
import tornadofx.keyboard
import tornadofx.label
import tornadofx.pane
import kotlin.system.measureTimeMillis

private const val TPS = 500.0
private val tickDiff: Double
    get() = 1000.0 / TPS

@Suppress("MemberVisibilityCanBePrivate")
class GameView : View("Ball Game") {
    lateinit var pane: Pane
        private set
    lateinit var user: Circle
        private set
    lateinit var enemy: Circle
        private set
    lateinit var topHBox: HBox
        private set
    lateinit var scoreLabel: Label
        private set
    lateinit var mainLabel: Label
        private set
    var active = false
    var gameStarted = false
    var pressed = mutableSetOf<KeyType>()
    var speed = 0.7
    var score = 0

    override val root = pane {
        active = true
        println("active")
        pane = this
        setupUserBall()
        setupEnemyBall()
        setupVariables()
        SCOPE.launch {
            while (active) {
                if (gameStarted) {
                    speed += 0.05
                    score++
                    Platform.runLater { scoreLabel.text = "Score: $score" }
                }
                delay(1000L)
            }
        }
        SCOPE.launch {
            delay(2999L)
            gameStarted = true
            while (active) {
                val off = measureTimeMillis {
                    move()
                    detectHit()
                }
                if (off > tickDiff) {
                    System.err.println("Running ${(off - tickDiff).toInt()}ms behind (${((off - tickDiff) / tickDiff).toInt()} ticks)!")
                    continue
                }
                delay(tickDiff.toInt() - off)
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
        scoreLabel = topHBox.label("Score: 0") { font = Font.font("System", 50.0) }
        topHBox.label("                  ") { font = Font.font("System", 70.0) }
        mainLabel = label("Tag") { font = Font.font("System", 70.0) }
        finished = true
    }

    private fun HBox.updateTopHBox() {
        mainLabel.teleport(centerX - (mainLabel.width / 2), 50.0)
        teleport(centerX - (width / 2), 50.0)
    }

    private fun Pane.setupEnemyBall() {
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
        val enemyLoc = enemy.getLocation()
        enemy.teleport(enemyLoc.getDirectionPoint(speed * 0.94, enemyLoc.getAngle(user.getLocation())))
    }

    private fun detectHit() {
        if (user.isTouching(enemy)) {
            println("touching")
            active = false
        }
    }
}