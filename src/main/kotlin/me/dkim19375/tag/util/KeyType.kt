package me.dkim19375.tag.util

enum class KeyType(val letter: Char, val angle: Double) {
    UP('W', 270.0),
    LEFT('A', 180.0),
    DOWN('S', 90.0),
    RIGHT('D', 360.0)
}

fun String.toKeyType(): KeyType? {
    return try {
        KeyType.valueOf(uppercase())
    } catch (_: IllegalArgumentException) {
        KeyType.values().firstOrNull { it.letter.equals(getOrElse(0) { ' ' }, true) }
    }
}