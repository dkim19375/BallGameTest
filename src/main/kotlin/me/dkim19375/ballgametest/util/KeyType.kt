package me.dkim19375.ballgametest.util

enum class KeyType {
    W,
    A,
    S,
    D
}

fun Char.toKeyType(): KeyType? = try {
    KeyType.valueOf(uppercase())
} catch (_: IllegalArgumentException) {
    null
}