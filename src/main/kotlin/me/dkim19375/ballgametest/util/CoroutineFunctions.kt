package me.dkim19375.ballgametest.util

fun await(boolean: () -> Boolean) {
    while (true) { // suppress warning lol
        if (boolean()) {
            return
        }
    }
}