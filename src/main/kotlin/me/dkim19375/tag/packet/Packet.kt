package me.dkim19375.tag.packet

import io.ktor.http.cio.websocket.*
import me.dkim19375.tag.multiplayer.ClientManager
import me.dkim19375.tag.multiplayer.Profile
import me.dkim19375.tag.multiplayer.ServerManager

interface Packet {
    /**
     * Execute a packet
     *
     * @param socket The [WebSocketSession] to send the packet to
     * @param text The text received
     * @param manager The [ClientManager]
     * @return A [Pair<Profile?, Profile?>][Pair] of the self profile and the other profile
     */
    suspend fun execute(socket: WebSocketSession, text: String?, manager: ClientManager): Pair<Profile?, Profile?>

    /**
     * Execute a packet
     *
     * @param socket The [WebSocketSession] to send the packet to
     * @param text The text received
     * @param manager The [ServerManager]
     * @return A [Pair<Profile?, Profile?>][Pair] of the self profile and the other profile
     */
    suspend fun execute(socket: WebSocketSession, text: String?, manager: ServerManager): Pair<Profile?, Profile?>
}