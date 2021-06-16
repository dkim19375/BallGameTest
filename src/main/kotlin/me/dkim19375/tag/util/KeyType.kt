package me.dkim19375.tag.util

enum class KeyType(val letter: Char) {
    UP('W'),
    LEFT('A'),
    DOWN('S'),
    RIGHT('D')
}

fun String.toKeyType(): KeyType? {
    return try {
        KeyType.valueOf(uppercase())
    } catch (_: IllegalArgumentException) {
        KeyType.values().firstOrNull { it.letter.equals(getOrElse(0) { ' ' }, true) }
    }
}