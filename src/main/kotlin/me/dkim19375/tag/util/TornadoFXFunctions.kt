package me.dkim19375.tag.util

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.ButtonBase
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.media.AudioClip
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
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
    AudioClip(main.javaClass.getResource("/sounds/$sound")?.toExternalForm()).play(volume)
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

fun Region.applyBackgroundSettings() {
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
    playSoundEffect(Sounds.BUTTON)
    onAction = action
}