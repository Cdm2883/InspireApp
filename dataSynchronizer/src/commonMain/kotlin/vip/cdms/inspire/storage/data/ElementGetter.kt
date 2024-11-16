package vip.cdms.inspire.storage.data

import vip.cdms.inspire.storage.data.KVStorage.Companion.resolve
import kotlin.reflect.KClass

abstract class ElementGetter internal constructor(
    private vararg val parents: String
) : StorageProvider {
    inline fun <reified T : Any> keyOf(vararg keys: String) = keyOf(T::class, keys = keys)
    fun <T : Any> keyOf(clazz: KClass<T>, vararg keys: String) = KVElement.Nullable(clazz, storage, resolve(*parents, *keys))
    inline fun <reified T : Any> keyOf(vararg keys: String, noinline default: () -> T) = keyOf(T::class, keys = keys, default)
    fun <T : Any> keyOf(clazz: KClass<T>, vararg keys: String, default: () -> T) = KVElement.NonNull(clazz, storage, resolve(*parents, *keys), default)

    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("this.keyOf<T>(\"\")"), level = DeprecationLevel.ERROR)
    fun <T> keyOf(): Nothing = throw UnsupportedOperationException()
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("this.keyOf<T>(\"\", default = default)"), level = DeprecationLevel.ERROR)
    fun <T: Any> keyOf(default: () -> T): Nothing = throw UnsupportedOperationException()
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("this.keyOf<T>(clazz, \"\")"), level = DeprecationLevel.ERROR)
    fun <T: Any> keyOf(clazz: KClass<T>): Nothing = throw UnsupportedOperationException()
    @Deprecated("At least pass a 'key' is needed.", ReplaceWith("this.keyOf<T>(clazz, \"\", default = default)"), level = DeprecationLevel.ERROR)
    fun <T: Any> keyOf(clazz: KClass<T>, default: () -> T): Nothing = throw UnsupportedOperationException()
}
