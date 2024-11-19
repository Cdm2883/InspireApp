package vip.cdms.inspire.storage.data

import java.util.prefs.Preferences

actual object StorageRoot : KVStorage() {
    private val preferences by lazy { Preferences.userRoot() }
    actual override fun setValue0(key: String, value: String?) = value?.let { preferences.put(key, it) } ?: preferences.remove(key)
    actual override fun getValue0(key: String): String? = preferences[key, null]
    actual override fun clear() = preferences.clear()
}
