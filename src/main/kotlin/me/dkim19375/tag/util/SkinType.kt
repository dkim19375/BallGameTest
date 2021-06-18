package me.dkim19375.tag.util

import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.paint.Paint
import me.dkim19375.tag.main

enum class SkinType(
    val intValue: Int,
    val cost: Int,
    val image: () -> Paint = { ImagePattern(Image("images/skins/$intValue.png")) }
) {
    DEFAULT(0, 0, { Color.BLACK }),
    ONE(1, 10),
    TWO(2, 30),
    THREE(3, 50),
    FOUR(4, 80),
    FIVE(5, 100),
    SIX(6, 200),
    CUSTOM(7, 300, { main.dataFile.getCurrentProfile().customSkin() });

    companion object {
        fun purchasableValues(): Array<SkinType> {
            val list = values().toMutableList()
            list.remove(DEFAULT)
            return list.toTypedArray()
        }

        fun getFromInt(int: Int): SkinType? = values().firstOrNull { it.intValue == int }
    }
}