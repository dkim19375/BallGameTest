package me.dkim19375.tag.util

enum class SkinType(
    val intValue: Int,
    val cost: Int,
    val imagePath: String = "images/skins/$intValue.png"
) {
    DEFAULT(0, 0, ""),
    ONE(1, 10),
    TWO(2, 30),
    THREE(3, 50),
    FOUR(4, 80),
    FIVE(5, 100),
    SIX(6, 200);

    companion object {
        fun purchasableValues(): Array<SkinType> {
            val list = values().toMutableList()
            list.remove(DEFAULT)
            return list.toTypedArray()
        }

        fun getFromInt(int: Int): SkinType? = values().firstOrNull { it.intValue == int }
    }
}