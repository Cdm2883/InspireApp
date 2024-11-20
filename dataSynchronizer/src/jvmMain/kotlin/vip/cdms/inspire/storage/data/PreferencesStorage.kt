package vip.cdms.inspire.storage.data

import java.util.prefs.Preferences

class PreferencesStorage(private val preferences: Preferences) : KVStorage() {
    override fun setValue0(key: String, value: String?) = value?.let { preferences.put(key, it) } ?: preferences.remove(key)
    override fun getValue0(key: String): String? = preferences[key, null]
    override fun clear() = preferences.clear()
}
