package me.dkim19375.tag.file

import javafx.scene.image.Image
import javafx.scene.paint.ImagePattern
import me.dkim19375.dkimcore.extension.runCatchingOrNull
import me.dkim19375.tag.util.Hash
import me.dkim19375.tag.util.SkinType

data class ProfileConfigData(
    var name: String = "Public",
    var password: String = "",
    var coins: Int = 0,
    var skins: Set<Int> = emptySet(),
    var selectedSkin: Int = SkinType.DEFAULT.intValue,
    var customSkin: String = Profile.CUSTOM_IMAGE,
    var highscore: Int = 0
) {
    fun toProfile(
        name: String = this.name,
        password: String = this.password,
        coins: Int = this.coins,
        skins: Set<Int> = this.skins,
        selectedSkin: Int = this.selectedSkin,
        customSkin: String = this.customSkin,
        highscore: Int = this.highscore
    ): Profile = Profile(
        name = name,
        password = Hash.deserialize(password),
        coins = coins,
        skins = skins.mapNotNull(SkinType::getFromInt).toSet(),
        selectedSkin = SkinType.getFromInt(selectedSkin) ?: SkinType.DEFAULT,
        customSkin = { ImagePattern(runCatchingOrNull { Image(customSkin) } ?: Image(Profile.CUSTOM_IMAGE)) },
        highscore = highscore
    )
}