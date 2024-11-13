package vip.cdms.inspire.storage.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.reflect.KClass

sealed class KVElement<T> private constructor(
    private val observer: MutableState<Boolean> = mutableStateOf(false)
): StorageProvider, MutableState<T> {

    abstract val key: String
    protected abstract fun update(value: T?)
    protected abstract fun update(): T

    protected fun observing() = storage.changeObserver.on(key) { recompose() }

    private fun subscribe() { observer.value }
    private fun recompose() { observer.value = !observer.value }

    override var value: T
        get() = update().also { subscribe() }
        set(value) { update(value); recompose() }
    override operator fun component1() = value
    override operator fun component2(): (T) -> Unit = { value = it }

    class Nullable<T : Any> internal constructor(
        private val clazz: KClass<T>,
        override val storage: KVStorage,
        override val key: String
    ) : KVElement<T?>() {
        init { observing() }
        override fun update(value: T?) = storage.set(clazz, key, value = value)
        override fun update() = storage.get(clazz, key)
    }

    class NonNull<T : Any> internal constructor(
        private val clazz: KClass<T>,
        override val storage: KVStorage,
        override val key: String,
        val default: () -> T
    ) : KVElement<T>() {
        init { observing() }
        override fun update(value: T?) = storage.set(clazz, key, value = value)
        override fun update() = storage.get(clazz, key) ?: default()
    }

}
