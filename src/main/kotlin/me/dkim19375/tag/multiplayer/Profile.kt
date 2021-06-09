package me.dkim19375.tag.multiplayer

import javafx.geometry.Point2D

@Suppress("DataClassPrivateConstructor")
data class Profile private constructor(
    val username: String,
    val isEnemy: Boolean,
    val isServer: Boolean,
    val location: Point2D = Point2D(0.0, 0.0),
    val lives: Int = 5,
) {
    companion object {
        private val cache = mutableSetOf<Profile>()

        @Synchronized
        fun getProfile(
            username: String,
            enemy: Boolean,
            isServer: Boolean,
            location: Point2D = Point2D(0.0, 0.0)
        ): Profile {
            val match = cache.firstOrNull {
                it.username == username
                        && it.isEnemy == enemy
                        && it.isServer == isServer
                        && it.location == location
            }
            if (match == null) {
                val new = Profile(username, enemy, isServer, location)
                cache.add(new)
                return new
            }
            return match
        }
    }
}