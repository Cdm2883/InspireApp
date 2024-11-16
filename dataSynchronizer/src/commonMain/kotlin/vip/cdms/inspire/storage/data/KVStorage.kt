package vip.cdms.inspire.storage.data

import kotlin.jvm.JvmStatic
import kotlin.reflect.KClass

abstract class KVStorage : ElementGetter() {

    /**
     * ```kt
     * object UserSettings : KVStorage.KeyOf("Settings", "User"),
     *     StorageProvider by StorageRoot {
     *     var name by store<String> { "Mike" }
     *     var age by a<Short>()
     *     // var age = store<Short>()  // only delegations are allowed
     *     var habit = keyOf<String>("habit")
     *     var description by keyOf<String>("description")
     * }
     * ```
     */
    abstract class KeyOf(vararg val keys: String) : ElementGetter(parents = keys) {
        constructor(parent: KeyOf, vararg keys: String): this(*parent.keys, *keys)
        fun resolve(vararg keys: String) = KVStorage.resolve(*this.keys, *keys)
        val key = resolve()
        // TODO: store
    }

    @Suppress("LeakingThis")
    override val storage = this

    protected abstract fun setValue0(key: String, value: String?)
    protected abstract fun getValue0(key: String): String?

    fun <T : Any> set(clazz: KClass<T>, vararg keys: String, value: T?) {
        val setting = value?.toString()  // TODO
        set(keys = keys, value = setting)
    }
    fun <T : Any> get(clazz: KClass<T>, vararg keys: String): T? {
        val value = get(keys = keys)
        return value as? T?  // TODO
    }

    internal class ChangeObserver {
        private val listeners = mutableMapOf<String, MutableList<() -> Unit>>()
        fun notify(key: String) = listeners[key]?.forEach { it() }
        fun on(key: String, callback: () -> Unit) = listeners.getOrPut(key) { mutableListOf() }.add(callback)
    }
    internal val changeObserver = ChangeObserver()
    operator fun set(vararg keys: String, value: String?) {
        val key = resolve(keys = keys)
        setValue0(key, value)
        changeObserver.notify(key)
    }
    operator fun get(vararg keys: String) = getValue0(resolve(keys = keys))

    inline fun <reified T : Any> set(vararg keys: String, value: T?) = set(T::class, keys = keys, value = value)
    inline fun <reified T : Any> get(vararg keys: String) = get(T::class, keys = keys)
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("get(\"\")"), level = DeprecationLevel.ERROR)
    fun get(): Nothing = throw UnsupportedOperationException()

    companion object {
        @JvmStatic
        var KeySeparator = "#_"  // filled in some short weird chars that hardly appears
        @JvmStatic
        fun resolve(vararg keys: String) = keys.joinToString(KeySeparator)
    }
}
