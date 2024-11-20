package vip.cdms.inspire.storage.data

import vip.cdms.inspire.utils.SelfJar
import java.util.*

actual object StorageRoot : KVStorage() {
    private val file by lazy { SelfJar.Location.resolve("preferences.properties") }
    private val properties by lazy { Properties().apply { loadFromFile() } }

    private fun Properties.loadFromFile() {
        if (file.exists()) file.inputStream().use { load(it) }
    }

    private fun Properties.saveToFile() {
        file.outputStream().use { store(it, null) }
    }

    actual override fun setValue0(key: String, value: String?) {
        if (value == null) properties.remove(key) else properties[key] = value
        properties.saveToFile()
    }

    actual override fun getValue0(key: String): String? = properties.getProperty(key)

    actual override fun clear() {
        properties.clear()
        properties.saveToFile()
    }
}
