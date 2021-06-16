package me.dkim19375.tag.file

import me.dkim19375.tag.util.Hash
import me.dkim19375.tag.util.SkinType

data class ProfileConfigData(
    var name: String = "Public",
    var password: String = "",
    var coins: Int = 0,
    var skins: Set<Int> = emptySet(),
    var selectedSkin: Int = SkinType.DEFAULT.intValue
) {
    fun toProfile(
        name: String = this.name,
        password: String = this.password,
        coins: Int = this.coins,
        skins: Set<Int> = this.skins,
        selectedSkin: Int = this.selectedSkin
    ): Profile = Profile(
        name = name,
        password = Hash.deserialize(password),
        coins = coins,
        skins = skins.mapNotNull(SkinType::getFromInt).toSet(),
        selectedSkin = SkinType.getFromInt(selectedSkin) ?: SkinType.DEFAULT
    )
}