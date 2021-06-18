package me.dkim19375.tag.util

import javafx.scene.input.KeyCode
import me.dkim19375.tag.util.KeyType.*

enum class KeyType(val angle: Double) {
    UP(270.0),
    LEFT(180.0),
    DOWN(90.0),
    RIGHT(360.0)
}

fun KeyCode.toKeyType(): KeyType? {
    return when (this) {
        KeyCode.W -> UP
        KeyCode.A -> LEFT
        KeyCode.S -> DOWN
        KeyCode.D -> RIGHT
        KeyCode.UP -> UP
        KeyCode.LEFT -> LEFT
        KeyCode.DOWN -> DOWN
        KeyCode.RIGHT -> RIGHT
        else -> null
    }
}