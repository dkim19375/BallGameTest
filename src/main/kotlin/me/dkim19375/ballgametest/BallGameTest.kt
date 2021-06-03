package me.dkim19375.ballgametest

import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.dkim19375.ballgametest.view.GameView
import tornadofx.App
import tornadofx.launch
import kotlin.system.exitProcess

lateinit var main: BallGameTest
    private set
val SCOPE: CoroutineScope = CoroutineScope(Dispatchers.Default)

fun main(args: Array<String>) {
    launch<BallGameTest>(*args)
}

class BallGameTest : App(GameView::class) {
    lateinit var stage: Stage
        private set
    lateinit var gameView: GameView
    var score = 0

    init {
        main = this
    }

    override fun start(stage: Stage) {
        this.stage = stage
        stage.isMaximized = true
        stage.height = 1080.0
        stage.width = 1920.0
        super.start(stage)
        // stage.icons.add(Image(javaClass.classLoader.getResourceAsStream("images/icon.png")))
    }

    override fun stop() {
        exitProcess(0)
    }
}