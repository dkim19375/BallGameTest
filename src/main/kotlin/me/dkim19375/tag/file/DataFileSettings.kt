package me.dkim19375.tag.file

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object DataFileSettings : SettingsHolder {
    @Path("profile")
    val PROFILE: Property<String> = Property.create("Public")

    @Path("profiles")
    val PROFILES: Property<Map<String, ProfileConfigData>> = Property.create(
        ProfileConfigData::class.java, mapOf("Public" to ProfileConfigData())
    )
}