package me.dkim19375.tag.file

import me.dkim19375.tag.TagGame
import me.dkim19375.tag.util.Hash
import me.dkim19375.tag.util.SkinType

data class Profile(
    val name: String = "Public",
    val password: Hash = Hash.emptyHash(),
    val coins: Int = 0,
    val skins: Set<SkinType> = emptySet(),
    val selectedSkin: SkinType = SkinType.DEFAULT
) {
    fun setSkins(skins: Set<SkinType>, main: TagGame = me.dkim19375.tag.main) {
        main.dataFile.setProfile(copy(skins = skins))
    }

    fun setSelectedSkin(skin: SkinType, main: TagGame = me.dkim19375.tag.main) {
        main.dataFile.setProfile(copy(selectedSkin = skin))
    }

    fun setCoins(coins: Int, main: TagGame = me.dkim19375.tag.main) {
        main.dataFile.setProfile(copy(coins = coins))
    }

    fun toConfigData(
        name: String = this.name,
        password: Hash = this.password,
        coins: Int = this.coins,
        skins: Set<SkinType> = this.skins,
        selectedSkin: SkinType = this.selectedSkin
    ): ProfileConfigData = ProfileConfigData(
        name = name,
        password = password.serialize(),
        coins = coins,
        skins = skins.map(SkinType::intValue).toSet(),
        selectedSkin = selectedSkin.intValue
    )
}