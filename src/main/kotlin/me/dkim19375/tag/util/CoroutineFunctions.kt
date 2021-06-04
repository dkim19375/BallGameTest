package me.dkim19375.tag.util

fun await(boolean: () -> Boolean) {
    while (true) { // suppress warning lol
        if (boolean()) {
            return
        }
    }
}