package me.dkim19375.tag.file

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.dkimcore.file.YamlFile
import me.dkim19375.tag.util.await

class DataFile : YamlFile(DataFileSettings, "data/datafile.yml") {
    private var saved = true

    init {
        reload()
        getProfile("Public") // check it exists
        SCOPE.launch {
            while (true) {
                await { !saved }
                saved = true
                save()
                delay(1000L)
            }
        }
    }

    fun getProfiles(): List<Profile> {
        val profiles = get(DataFileSettings.PROFILES).values.toMutableList()
        if (profiles.none { it.name == "Public" }) {
            val profile = Profile("Public")
            setProfile(profile)
            profiles.add(profile.toConfigData())
        }
        return profiles.map(ProfileConfigData::toProfile)
    }

    fun getProfileName(): String = get(DataFileSettings.PROFILE)

    fun getCurrentProfile(skipPassProtected: Boolean = false): Profile {
        val profile = getProfile(getProfileName())
        if (profile.password.array.isNotEmpty() && skipPassProtected) {
            setCurrentProfile("Public")
            return getProfile("Public")
        }
        return profile
    }

    fun setCurrentProfile(profile: String) {
        if (getProfileName() == profile) {
            return
        }
        set(DataFileSettings.PROFILE, profile)
        saved = false
    }

    fun getProfile(profile: String): Profile {
        if (get(DataFileSettings.PROFILES).containsKey(profile)) {
            return get(DataFileSettings.PROFILES).getValue(profile).toProfile()
        }
        val new = Profile(profile)
        setProfile(new)
        return new
    }

    fun profileExists(profile: String): Boolean = getProfiles().any { it.name == profile }

    fun setProfile(
        profile: Profile,
    ) {
        if (get(DataFileSettings.PROFILES).containsKey(profile.name)) {
            if (get(DataFileSettings.PROFILES)[profile.name]?.toProfile() == profile) {
                return
            }
        }
        val new = get(DataFileSettings.PROFILES).toMutableMap()
        new[profile.name] = profile.toConfigData()
        set(DataFileSettings.PROFILES, new)
        saved = false
    }

    fun deleteProfile(profile: String) {
        if (!get(DataFileSettings.PROFILES).containsKey(profile)) {
            return
        }
        val map = get(DataFileSettings.PROFILES).toMutableMap()
        map.remove(profile)
        set(DataFileSettings.PROFILES, map)
        saved = false
    }
}