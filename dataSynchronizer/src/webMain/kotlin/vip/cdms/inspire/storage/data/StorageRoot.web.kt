package vip.cdms.inspire.storage.data

import web.localStorage

actual object StorageRoot : KVStorage() {
    actual override fun setValue0(key: String, value: String?) = value?.let { localStorage.setItem(key, it) } ?: localStorage.removeItem(key)
    actual override fun getValue0(key: String) = localStorage.getItem(key)
    actual override fun clear() = localStorage.clear()
}
