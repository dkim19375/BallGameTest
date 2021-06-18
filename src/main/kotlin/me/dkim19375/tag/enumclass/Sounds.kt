package me.dkim19375.tag.enumclass

enum class Sounds(val path: String, val volume: Double = 1.0) {
    BUTTON("/sounds/button.mp3"),
    COIN("/sounds/coin.wav", 0.5)
}