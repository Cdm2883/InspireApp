package vip.cdms.inspire.storage.data

import android.content.Context
import androidx.core.content.edit
import vip.cdms.inspire.utils.GlobalContext

actual object StorageRoot : KVStorage() {
    private val sharedPreferences by lazy {
        // PreferenceManager.getDefaultSharedPreferences(GlobalContext!!)
        GlobalContext!!.getSharedPreferences(GlobalContext!!.packageName + "_preferences", Context.MODE_PRIVATE)
    }
    actual override fun setValue0(key: String, value: String?) = sharedPreferences.edit {
        value?.let { putString(key, it) } ?: remove(key)
        commit()
    }
    actual override fun getValue0(key: String) = sharedPreferences.getString(key, null)
    actual override fun clear() { sharedPreferences.edit().clear().commit() }
}
