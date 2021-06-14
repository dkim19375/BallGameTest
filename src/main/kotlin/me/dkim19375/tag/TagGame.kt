package me.dkim19375.tag

import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.dkim19375.tag.util.SkinType
import me.dkim19375.tag.view.GameEndView
import me.dkim19375.tag.view.GameView
import me.dkim19375.tag.view.SkinsView
import me.dkim19375.tag.view.StartView
import tornadofx.App
import tornadofx.launch
import kotlin.system.exitProcess

lateinit var main: Tag
    private set
val SCOPE: CoroutineScope = CoroutineScope(Dispatchers.Default)
const val VIEW_TITLE = "Tag Game"
lateinit var THREAD: Thread
    private set

fun main(args: Array<String>) {
    launch<Tag>(*args)
}

@Suppress("MemberVisibilityCanBePrivate")
class Tag : App(StartView::class) {
    lateinit var stage: Stage
        private set
    lateinit var gameEndView: GameEndView
    lateinit var gameView: GameView
    lateinit var startView: StartView
    lateinit var skinsView: SkinsView
    var selectedSkin: SkinType = SkinType.DEFAULT
    val owned = mutableSetOf<Int>()
    var coins = 0
    var score = 0

    init {
        main = this
    }

    override fun start(stage: Stage) {
        THREAD = Thread.currentThread()
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