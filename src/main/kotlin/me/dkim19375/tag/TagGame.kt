package me.dkim19375.tag

import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.ServerManager
import me.dkim19375.tag.view.GameView
import me.dkim19375.tag.view.LobbyView
import me.dkim19375.tag.view.StartView
import tornadofx.App
import tornadofx.launch
import kotlin.system.exitProcess

lateinit var main: BallGameTest
    private set
val SCOPE: CoroutineScope = CoroutineScope(Dispatchers.Default)
const val VIEW_TITLE = "Tag Game"

fun main(args: Array<String>) {
    launch<BallGameTest>(*args)
}

@Suppress("MemberVisibilityCanBePrivate")
class BallGameTest : App(StartView::class) {
    lateinit var stage: Stage
        private set
    lateinit var gameView: GameView
    lateinit var lobbyView: LobbyView
    val serverManager = ServerManager()
    val clientManager = ClientManager()
    var score = 0
    var multiScore = 0

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
        serverManager.stop()
        exitProcess(0)
    }
}