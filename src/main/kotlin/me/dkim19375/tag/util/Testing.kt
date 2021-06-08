package me.dkim19375.tag.util

import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


// testing server - WHY DOES THIS WORK AND MY PROGRAM DOESN'T AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
fun main() {
    println("program")
    CoroutineScope(Dispatchers.IO).launch {
        println("test 1, is active: $isActive")
        HttpClient {
            install(WebSockets)
        }.webSocket(method = HttpMethod.Get, port = 25575, path = "/tag") {
            println("test 2, is active: $isActive")
        }
        println("client stopped")
    }
}