package me.dkim19375.tag.view

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.tag.SCOPE
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.packet.out.GameStartPacketOut
import tornadofx.View
import tornadofx.hide
import tornadofx.show

class LobbyView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val otherLabel: Label by fxid()
    val startButton: Button by fxid()
    private val port: TextField by fxid()
    private val portText: Label by fxid()
    val profile: Profile?
        get() = run {
            val server = main.serverManager.profile
            val client = main.clientManager.profile
            return when {
                client != null -> client
                main.serverManager.enabled -> server
                else -> null
            }
        }
    val otherProfile: Profile?
        get() = run {
            val server = main.serverManager.otherProfile
            val client = main.clientManager.otherProfile
            return server ?: client
        }
    private val isServer: Boolean
        get() = profile?.isServer == true
    private val isClient: Boolean
        get() = profile?.isServer == false
    private var opened = false

    fun reset() {
        printDebug()
        startButton.show()
        startButton.text = "Open Lobby"
        if (isClient) {
            port.hide()
            portText.hide()
            startButton.hide()
        }
        port.text = main.serverManager.port.toString()
    }

    fun printDebug() {
        println("profile: $profile, otherProfile: $otherProfile, isServer: $isServer, isClient: $isClient")
    }

    init {
        main.lobbyView = this
        println("init")
        reset()
        SCOPE.launch {
            while (true) {
                Platform.runLater {
                    otherLabel.text = "Other Player: ${otherProfile?.username ?: "None"}"
                }
                if (otherProfile != null && isServer) {
                    startButton.show()
                }
                delay(100L)
            }
        }
        startButton.setOnAction {
            if (!opened) {
                opened = true
                main.serverManager.start(port.text.toIntOrNull() ?: main.serverManager.port)
                startButton.text = "Start Game"
                startButton.hide()
                return@setOnAction
            }
            SCOPE.launch {
                main.serverManager.handlePacket(GameStartPacketOut())
            }
        }
    }
}