package me.dkim19375.tag.util

import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.event.EventTarget
import javafx.scene.control.ButtonBase
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import kfoenix.jfxbutton
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.tag.enumclass.Sounds
import me.dkim19375.tag.main

fun playSoundEffect(sound: String, volume: Double = 1.0) {
    if (onMainThread()) {
        SCOPE.launch {
            playSoundEffect(sound, volume)
        }
        return
    }
    // AudioClip(main.javaClass.getResource("/sounds/$sound")?.toExternalForm()).play(volume)
    val player = MediaPlayer(Media(main.javaClass.getResource("/sounds/$sound")?.toExternalForm()))
    player.volume = volume
    player.play()
}

fun playSoundEffect(type: Sounds) {
    playSoundEffect(type.path.removePrefix("/sounds/"), type.volume)
}

fun playMusic(type: Sounds) {
    playMusic(type.path.removePrefix("/music/"), type.volume)
}

fun playMusic(music: String, volume: Double = 1.0) {
    if (onMainThread()) {
        SCOPE.launch {
            playMusic(music, volume)
        }
        return
    }
    val player = MediaPlayer(Media(main.javaClass.getResource("/music/$music")?.toExternalForm()))
    player.volume = volume
    player.play()
}

private val appliedBackgrounds = mutableSetOf<String>()

fun Region.applyBackgroundSettings() {
    val name = Thread.currentThread().stackTrace[2].className
    if (!appliedBackgrounds.contains(name)) {
        appliedBackgrounds.add(name)
        setOnMousePressed {
            requestFocus()
        }
    }
    Platform.runLater {
        requestFocus()
    }
    background = Background(
        BackgroundImage(
            Image("images/background.png"),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT
        )
    )
}

fun ButtonBase.setOnPress(action: EventHandler<ActionEvent>) {
    setOnAction {
        action.handle(it)
        playSoundEffect(Sounds.BUTTON)
    }
}

fun EventTarget.kfxButton(
    name: String,
    parent: EventTarget = this,
    height: Double = 50.0,
    width: Double = 350.0,
    fontType: FontWeight = FontWeight.BOLD,
    button: JFXButton.() -> Unit = {}
) = parent.jfxbutton(name, JFXButton.ButtonType.RAISED) button@{
    val borderRadius = 15.0
    background = Background(BackgroundFill(Color.BLACK, CornerRadii(borderRadius * 1.2), null))
    textFill = TEXT_COLOR
    border =
        Border(
            BorderStroke(
                BORDER_COLOR, BorderStrokeStyle.SOLID, CornerRadii(borderRadius), BorderWidths(5.0)
            )
        )
    this.prefHeight = height
    this.prefWidth = width
    font = Font.font(font.family, fontType, 24.0)
    button()
}