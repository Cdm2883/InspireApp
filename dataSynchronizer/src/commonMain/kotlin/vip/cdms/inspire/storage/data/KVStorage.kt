package vip.cdms.inspire.storage.data

import kotlin.jvm.JvmStatic
import kotlin.reflect.KClass

abstract class KVStorage : StorageProvider {

    /**
     * ```kt
     * object UserSettings : KVStorage.KeyOf("Settings", "User"),
     *     StorageProvider by StorageRoot {
     *     var name by store<String> { "Mike" }
     *     var age by store<Short>()
     *     // var age = store<Short>()  // only delegations are allowed
     *     var habit = keyOf<String>
     *     var description by keyOf<String>
     * }
     * ```
     */
    abstract class KeyOf(vararg val keys: String) : StorageProvider {
        // TODO: KVStorage.KeyOf
    }

    @Suppress("LeakingThis")
    override val storage = this

    protected abstract fun setValue0(key: String, value: String?)
    protected abstract fun getValue0(key: String): String?

    fun <T : Any> set(@Suppress("UNUSED_PARAMETER") clazz: KClass<T>, vararg keys: String, value: T?) {
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

    inline fun <reified T : Any> keyOf(vararg keys: String) = keyOf(T::class, keys = keys)
    fun <T : Any> keyOf(clazz: KClass<T>, vararg keys: String) = KVElement.Nullable(clazz, this, resolve(keys = keys))
    inline fun <reified T : Any> keyOf(vararg keys: String, noinline default: () -> T) = keyOf(T::class, keys = keys, default)
    fun <T : Any> keyOf(clazz: KClass<T>, vararg keys: String, default: () -> T) = KVElement.NonNull(clazz, this, resolve(keys = keys), default)

    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("get(\"\")"), level = DeprecationLevel.ERROR)
    fun get(): Nothing = throw UnsupportedOperationException()
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("this.keyOf<T>(\"\")"), level = DeprecationLevel.ERROR)
    fun <T> keyOf(): Nothing = throw UnsupportedOperationException()
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("this.keyOf<T>(\"\", default = default)"), level = DeprecationLevel.ERROR)
    fun <T: Any> keyOf(default: () -> T): Nothing = throw UnsupportedOperationException()
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("this.keyOf<T>(clazz, \"\")"), level = DeprecationLevel.ERROR)
    fun <T: Any> keyOf(clazz: KClass<T>): Nothing = throw UnsupportedOperationException()
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("this.keyOf<T>(clazz, \"\", default = default)"), level = DeprecationLevel.ERROR)
    fun <T: Any> keyOf(clazz: KClass<T>, default: () -> T): Nothing = throw UnsupportedOperationException()

    companion object {
        @JvmStatic
        var KeySeparator = "#_"  // filled in some short weird chars that hardly appears
        @JvmStatic
        fun resolve(vararg keys: String) = keys.joinToString(KeySeparator)
    }
}
