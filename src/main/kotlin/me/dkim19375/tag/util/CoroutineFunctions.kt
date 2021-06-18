package me.dkim19375.tag.util

import kotlin.coroutines.suspendCoroutine

suspend fun await(boolean: () -> Boolean) {
    suspendCoroutine<Unit> {
        while (true) { // suppress warning lol
            if (boolean()) {
                runCatching { it.resumeWith(Result.success(Unit)) }
                return@suspendCoroutine
            }
        }
    }
}