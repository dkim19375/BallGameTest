package me.dkim19375.tag.file

import javafx.scene.image.Image
import javafx.scene.paint.ImagePattern
import me.dkim19375.tag.TagGame
import me.dkim19375.tag.util.Hash
import me.dkim19375.tag.enumclass.SkinType

data class Profile(
    val name: String = "Public",
    val password: Hash = Hash.emptyHash(),
    val coins: Int = 0,
    val skins: Set<SkinType> = emptySet(),
    val selectedSkin: SkinType = SkinType.DEFAULT,
    val customSkin: () -> ImagePattern = { ImagePattern(Image(CUSTOM_IMAGE)) },
    val highscore: Int = 0
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

    fun setCustomSkin(loc: String?, main: TagGame = me.dkim19375.tag.main) {
        val newLoc = loc?.run { "file:".plus(removePrefix("file:")) }
        main.dataFile.setProfile(copy(customSkin = { ImagePattern(Image(newLoc ?: CUSTOM_IMAGE)) }))
    }

    fun setHighscore(highscore: Int, main: TagGame = me.dkim19375.tag.main) {
        main.dataFile.setProfile(copy(highscore = highscore))
    }

    fun toConfigData(
        name: String = this.name,
        password: Hash = this.password,
        coins: Int = this.coins,
        skins: Set<SkinType> = this.skins,
        selectedSkin: SkinType = this.selectedSkin,
        customSkin: ImagePattern = this.customSkin(),
        highscore: Int = this.highscore
    ): ProfileConfigData = ProfileConfigData(
        name = name,
        password = password.serialize(),
        coins = coins,
        skins = skins.map(SkinType::intValue).toSet(),
        selectedSkin = selectedSkin.intValue,
        customSkin = customSkin.image.url,
        highscore = highscore
    )

    companion object {
        const val CUSTOM_IMAGE = "/images/skins/Custom.png"
    }
}