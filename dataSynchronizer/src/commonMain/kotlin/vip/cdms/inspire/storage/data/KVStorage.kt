package vip.cdms.inspire.storage.data

import kotlin.jvm.JvmStatic
import kotlin.reflect.KClass

abstract class KVStorage : ElementGetter() {

    /**
     * ```kt
     * object UserSettings : KVStorage.KeyOf("Settings", "User"),
     *     StorageProvider by StorageRoot {
     *     var name = keyOf<String>("nickname") { "Mike" }
     *     var age by keyOf<Int>("age")
     * }
     * ```
     */
    abstract class KeyOf(vararg val keys: String) : ElementGetter(parents = keys) {
        constructor(parent: KeyOf, vararg keys: String): this(*parent.keys, *keys)
        abstract inner class Nesting(vararg keys: String) : KeyOf(this, keys = keys), StorageProvider by this
        fun resolve(vararg keys: String) = KVStorage.resolve(*this.keys, *keys)
        val key = resolve()
    }

    /** Just the KVStorage itself */
    @Deprecated("Redundant calls", ReplaceWith("this"))
    @Suppress("LeakingThis")
    override val storage = this

    protected abstract fun setValue0(key: String, value: String?)
    protected abstract fun getValue0(key: String): String?
    protected abstract fun clear()

    internal class ChangeObserver {
        private val listeners = mutableMapOf<String, MutableList<() -> Unit>>()
        fun notify(key: String) = listeners[key]?.forEach { it() }
        fun on(key: String, callback: () -> Unit) = listeners.getOrPut(key) { mutableListOf() }.add(callback)
    }
    internal val changeObserver = ChangeObserver()
    @Deprecated("Never write the raw content directly unless you know what you're doing!")
    fun setRaw(vararg keys: String, value: String?) {
        val key = resolve(keys = keys)
        setValue0(key, value)
        changeObserver.notify(key)
    }
    @Deprecated("Never read the raw content directly unless you know what you're doing!", ReplaceWith("this.get<String>(*keys)"))
    fun getRaw(vararg keys: String) = getValue0(resolve(keys = keys))

    fun <T : Any> set(clazz: KClass<T>, vararg keys: String, value: T?) {
        @Suppress("DEPRECATION")
        if (value == null) return setRaw(keys = keys, value = null)
        @Suppress("DEPRECATION")
        setRaw(keys = keys, value = StorageSerialization.encode(clazz, value))
    }
    fun <T : Any> get(clazz: KClass<T>, vararg keys: String): T? {
        @Suppress("DEPRECATION")
        val value = getRaw(keys = keys) ?: return null
        return StorageSerialization.decode(clazz, value)
    }

    inline operator fun <reified T : Any> set(vararg keys: String, value: T?) = set(T::class, keys = keys, value = value)
    inline operator fun <reified T : Any> get(vararg keys: String) = get(T::class, keys = keys)
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("get(\"\")"), DeprecationLevel.ERROR)
    fun get(): Nothing = throw UnsupportedOperationException()

    companion object {
        @JvmStatic
        var KeySeparator = "#_"  // filled in some short weird chars that hardly appears
        @JvmStatic
        fun resolve(vararg keys: String) = keys.joinToString(KeySeparator)
    }
}
