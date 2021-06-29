package me.dkim19375.tag.file

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.IO_SCOPE
import me.dkim19375.dkimcore.file.JsonFile

@Suppress("MemberVisibilityCanBePrivate")
class DataFile : JsonFile<DataFileData>(DataFileData::class, "data/datafile.json") {
    private var saved = true

    private val currentProfileName: String
        get() = get().profile

    private val profileMap: Map<String, ProfileConfigData>
        get() = get().profiles

    init {
        reload()
        getProfile("Public") // check it exists
        IO_SCOPE.launch {
            while (true) {
                if (saved) {
                    continue
                }
                saved = true
                save()
                delay(1000L)
            }
        }
    }

    fun getProfiles(): List<Profile> {
        val profiles = profileMap.values.toMutableList()
        if (profiles.none { it.name == "Public" }) {
            val profile = Profile("Public")
            setProfile(profile)
            profiles.add(profile.toConfigData())
        }
        return profiles.map(ProfileConfigData::toProfile)
    }

    fun getProfileName(): String = currentProfileName

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
        set(get().copy(profile = profile))
        saved = false
    }

    fun getProfile(profile: String): Profile {
        if (profileMap.containsKey(profile)) {
            return profileMap.getValue(profile).toProfile()
        }
        val new = Profile(profile)
        setProfile(new)
        return new
    }

    @Suppress("unused")
    fun profileExists(profile: String): Boolean = getProfiles().any { it.name == profile }

    fun setProfile(
        profile: Profile,
    ) {
        if (profileMap.containsKey(profile.name)) {
            if (profileMap[profile.name]?.toProfile() == profile) {
                return
            }
        }
        val new = profileMap.toMutableMap()
        new[profile.name] = profile.toConfigData()
        set(get().copy(profiles = new))
        saved = false
    }

    fun deleteProfile(profile: String) {
        if (!profileMap.containsKey(profile)) {
            return
        }
        val map = profileMap.toMutableMap()
        map.remove(profile)
        set(get().copy(profiles = map))
        saved = false
    }
}