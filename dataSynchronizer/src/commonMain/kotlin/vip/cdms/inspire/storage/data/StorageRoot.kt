package vip.cdms.inspire.storage.data

/**
 * implemented natively across platforms **KVStorage**
 */
expect object StorageRoot : KVStorage {
    override fun setValue0(key: String, value: String?)
    override fun getValue0(key: String): String?
    override fun clear()
}
