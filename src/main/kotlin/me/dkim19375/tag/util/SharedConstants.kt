package me.dkim19375.tag.util

import javafx.scene.paint.Color

const val TPS = 500.0
val tickDiff: Double
    get() = 1000.0 / TPS
const val BASE_SPEED = 0.6
const val VIEW_TITLE = "Tag Game"
const val TEXT_COLOR_HEX = "#596aff"
val TEXT_COLOR: Color = Color.web(TEXT_COLOR_HEX)