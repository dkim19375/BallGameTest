package me.dkim19375.tag.util

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