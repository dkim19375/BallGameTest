package me.dkim19375.tag

import javafx.stage.Stage
import me.dkim19375.tag.file.DataFile
import me.dkim19375.tag.file.Profile
import me.dkim19375.tag.manager.GameManager
import me.dkim19375.tag.util.SkinType
import me.dkim19375.tag.view.*
import tornadofx.App
import tornadofx.launch
import kotlin.system.exitProcess

lateinit var main: TagGame
    private set
lateinit var THREAD: Thread
    private set

fun main(args: Array<String>) {
    System.setProperty("javafx.animation.fullspeed", "true")
    launch<TagGame>(*args)
}

@Suppress("MemberVisibilityCanBePrivate")
class TagGame : App(StartView::class) {
    val dataFile: DataFile = DataFile()
    lateinit var stage: Stage
        private set
    lateinit var gameEndView: GameEndView
    lateinit var gameView: GameView
    lateinit var startView: StartView
    lateinit var skinsView: SkinsView
    lateinit var profileView: ProfileView
    lateinit var createAccountView: CreateAccountView
    val gameManager: GameManager = GameManager(this)
    private var first = true
    val profile: Profile
        get() = run {
            val profile = dataFile.getCurrentProfile(first)
            first = false
            profile
        }
    var selectedSkin: SkinType
        get() = profile.selectedSkin
        set(value) = run { profile.setSelectedSkin(value) }
    var owned: Set<SkinType>
        get() = profile.skins
        set(value) = run { profile.setSkins(value, this@TagGame) }
    var coins: Int
        get() = profile.coins
        set(value) = run { profile.setCoins(value, this@TagGame) }
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