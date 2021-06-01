package me.dkim19375.ballgametest.view

import javafx.scene.input.KeyEvent
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.ballgametest.SCOPE
import me.dkim19375.ballgametest.util.*
import tornadofx.View
import tornadofx.circle
import tornadofx.keyboard
import tornadofx.pane
import kotlin.system.measureTimeMillis

private const val TPS = 500.0
private val tickDiff: Double
    get() = 1000.0 / TPS

@Suppress("MemberVisibilityCanBePrivate")
class GameView : View("Ball Game") {
    lateinit var pane: Pane
        private set
    lateinit var ball: Circle
        private set
    var active = false
    var pressed = mutableSetOf<KeyType>()
    var speed = 0.7

    override val root = pane {
        active = true
        println("active")
        pane = this
        ball = circle(centerX = 0.getX(), centerY = 0.getY(), radius = 60)
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) { event ->
                val char = try {
                    if (event.text.length > 1) {
                        throw IndexOutOfBoundsException()
                    }
                    event.text[0]
                } catch (_: IndexOutOfBoundsException) {
                    return@addEventHandler
                }
                println("got $char")
                val type = char.toKeyType() ?: return@addEventHandler
                pressed.add(type)
                println("added $char to pressed - $pressed")
            }
            addEventHandler(KeyEvent.KEY_RELEASED) { event ->
                val char = try {
                    if (event.text.length > 1) {
                        throw IndexOutOfBoundsException()
                    }
                    event.text[0]
                } catch (_: IndexOutOfBoundsException) {
                    return@addEventHandler
                }
                println("released $char")
                val type = char.toKeyType() ?: return@addEventHandler
                pressed.remove(type)
                println("removed $char to pressed - $pressed")
            }
        }
        SCOPE.launch {
            while (active) {
                speed += 0.05
                println("loc: ${ball.getLocation()}")
                println("bounded: ${ball.getLocation().setBounds()}")
                println(
                    "bounded2: ${
                        ball.getLocation().setBounds(
                            maxX = windowX - (ball.radius * 0.8),
                            maxY = windowY - (ball.radius * 0.8),
                            minX = (ball.radius * 1.1),
                            minY = (ball.radius * 1.1)
                        )
                    }"
                )
                delay(1000L)
            }
        }
        SCOPE.launch {
            while (active) {
                val off = measureTimeMillis {
                    move()
                }
                if (off > tickDiff) {
                    System.err.println("Running ${off - tickDiff} ticks behind!")
                    continue
                }
                delay(tickDiff.toInt() - off)
            }
        }
    }

    fun move() {
        pressed.toSet().forEach { type ->
            ball.teleport(ball.getPoint(type, speed))
        }
    }
}