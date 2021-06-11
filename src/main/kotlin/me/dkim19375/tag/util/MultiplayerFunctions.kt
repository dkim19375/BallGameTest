package me.dkim19375.tag.util

import me.dkim19375.tag.main
import me.dkim19375.tag.packet.Packet

fun Packet.sendDebugPacketMsg(text: String?) {
    val className = javaClass.simpleName
    val out = className.endsWith("Out")
    println("Packet $className ${if (out) "sending" else "receiving"}, text: ${text ?: "NULL"}")
    if (main::lobbyView.isInit()) {
        main.lobbyView.printDebug()
    } else {
        println("lobbyView not init!")
    }
}