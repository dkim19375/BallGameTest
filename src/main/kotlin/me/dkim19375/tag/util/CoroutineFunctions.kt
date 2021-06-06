package me.dkim19375.tag.util

import kotlinx.coroutines.delay

fun await(boolean: () -> Boolean) {
    while (true) { // suppress warning lol
        if (boolean()) {
            return
        }
    }
}

suspend fun <T> awaitUntilNonnull(delay: Long = 0L, action: () -> T?): T {
    while (true) { // suppress warning lol
        val value = action()
        if (value != null) {
            return value
        }
        delay(delay)
    }
}