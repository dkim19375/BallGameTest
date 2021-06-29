package me.dkim19375.tag.file

data class DataFileData(
    val profile: String = "Public",
    val profiles: Map<String, ProfileConfigData> = mapOf("Public" to ProfileConfigData())
)