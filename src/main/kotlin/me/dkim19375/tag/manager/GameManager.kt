package me.dkim19375.tag.manager

import javafx.application.Platform
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode
import javafx.scene.shape.Circle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.tag.TagGame
import me.dkim19375.tag.enumclass.KeyType
import me.dkim19375.tag.enumclass.Sounds
import me.dkim19375.tag.enumclass.toKeyType
import me.dkim19375.tag.util.*
import me.dkim19375.tag.view.GameEndView
import me.dkim19375.tag.view.GameView
import tornadofx.hide
import tornadofx.show
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.system.measureTimeMillis

class GameManager(private val main: TagGame) {
    var active = false
    var latestTPS = TPS.toInt()
    var gameStarted = false
    val pressed = mutableSetOf<KeyType>()
    var speed = BASE_SPEED * (500.0 / TPS)
    var lives = 5
    var coins = 0
    var enemyFrozen = false
    var lastHit = 0L
    val gameView: GameView
        get() = main.gameView
    val user: Circle
        get() = gameView.user
    val enemy: Circle
        get() = gameView.enemy
    val coin: Circle
        get() = gameView.coin

    private fun reset() {
        active = false
        gameStarted = false
        pressed.clear()
        speed = BASE_SPEED * (500.0 / TPS)
        main.score = 0
        lives = 5
        coins = 0
        enemyFrozen = false
        if (this::gameView.isInit()) {
            gameView.reset()
        }
    }

    fun start() {
        if (!onMainThread()) {
            Platform.runLater {
                start()
            }
            return
        }
        reset()
        gameView.setupUserBall()
        user.teleport((-200).getX(), 0.getY())
        gameView.setupEnemyBall()
        enemy.teleport(200.getX(), 0.getY())
        gameView.setupLabels {
            SCOPE.launch {
                while (true) {
                    runSync {
                        gameView.updateTopVBox()
                        gameView.gameOverLabel.run {
                            teleport(centerX - (width / 2), windowY - height - 100.0)
                        }
                    }
                    delay(50L)
                }
            }
        }
        gameView.setupCoin()
        gameView.coin.show()
        teleportCoin()
        active = true
        SCOPE.launch {
            launch {
                setupGameLoop()
            }
            launch {
                setupTicking()
            }
        }
    }

    private suspend fun setupGameLoop() {
        var first = true
        while (true) {
            if (!active) {
                return
            }
            if (gameStarted && !first && !enemyFrozen) {
                speed += 0.05 * (500.0 / TPS)
                main.score++
            }
            gameView.updateLabels()
            first = false
            delay(1000L)
        }
    }

    private suspend fun setupTicking() {
        delay(3000L)
        gameStarted = true
        while (true) {
            if (!active) {
                return
            }
            val off = measureTimeMillis {
                tick()
            }
            if ((off > tickDiff) && ((off - tickDiff) / tickDiff >= 10.0)) {
                System.err.println("Running ${(off - tickDiff).toInt()}ms behind (${((off - tickDiff) / tickDiff).toInt()} ticks)!")
            }
            latestTPS = min((TPS.toInt() - ((off - tickDiff) / tickDiff).toInt()), TPS.toInt())
            delay((tickDiff - off).toLong())
        }
    }

    private fun tick() {
        runSync {
            move()
            detectCoin()
            detectHit()
        }
    }

    @Synchronized
    private fun move() {
        val loc = user.getPoint(pressed, speed).setBounds(
            maxX = windowX - user.radius * 1.5,
            maxY = windowY - user.radius * 1.5,
            minX = -(user.radius * 0.5),
            minY = -(user.radius * 0.5)
        )
        user.teleport(loc)
        if (!enemyFrozen) {
            val enemyLoc = enemy.getLocation()
            enemy.teleport(enemyLoc.getDirectionPoint(speed * 0.8, enemyLoc.getAngle(user.getLocation())))
        }
    }

    @Synchronized
    private fun detectCoin() = runSync {
        if (!coin.isVisible) {
            return@runSync
        }
        if (!user.isTouching(coin)) {
            return@runSync
        }
        playSoundEffect(Sounds.COIN)
        coins++
        gameView.updateLabels()
        teleportCoin()
    }

    private fun teleportCoin() {
        val radius = coin.radius
        val loc = coin.getLocation()
        val min = -(radius * 0.5).toInt()
        val max = (radius * 1.5)
        var point: Point2D
        while (true) {
            point = Point2D(
                Random.nextInt(min..(windowX - max).toInt()).toDouble(),
                Random.nextInt(min..(windowY - max).toInt()).toDouble()
            )
            val distanceX = windowX / 3
            val distanceY = windowY / 3
            if (max(point.x, loc.x) - min(point.x, loc.x) > distanceX) {
                continue
            }
            if (max(point.y, loc.y) - min(point.y, loc.y) > distanceY) {
                continue
            }
            break
        }
        coin.teleport(point)
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
        if (System.currentTimeMillis() - lastHit <= 2500L) {
            return
        }
        lastHit = System.currentTimeMillis()
        lives--
        coin.hide()
        gameView.updateLabels()
        SCOPE.launch {
            enemyFrozen = true
            delay(3000L)
            enemyFrozen = false
            runSync {
                coin.show()
                teleportCoin()
            }
        }
    }

    private fun stop() {
        gameView.stop()
        active = false
        main.coins += coins
        main.startView.updateCoinsLabel()
        main.startView.updateHighscoreLabel()
        SCOPE.launch {
            delay(3000L)
            Platform.runLater {
                gameView.replaceWith<GameEndView>()
                val highscore = main.profile.highscore
                if (main.score > highscore) {
                    main.profile.setHighscore(main.score)
                    main.startView.updateHighscoreLabel()
                }
                main.gameEndView.start(coins, highscore)
            }
        }
    }

    fun onKeyPressed(key: KeyCode) = runSync {
        pressed.add(key.toKeyType() ?: return@runSync)
    }

    fun onKeyReleased(key: KeyCode) = runSync {
        pressed.remove(key.toKeyType() ?: return@runSync)
    }
}