package vip.cdms.inspire.storage.data

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.serialization.Serializable
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class KVStorageTest {

    private object TestStorage : KVStorage() {
        val map = mutableMapOf<String, String?>()
        override fun setValue0(key: String, value: String?) { map[key] = value }
        override fun getValue0(key: String) = map[key]
        override fun clear() = map.clear()
    }
    @BeforeTest
    fun clearStorageContent() = TestStorage.map.clear()
    @AfterTest
    fun printStorageContent() = println("\n${TestStorage.map.entries.joinToString("\n") { "${it.key} -> ${it.value}" }}\n")

    @Serializable
    data class TestBundle(val name: String? = "Default", val test: Boolean)
    @Test
    fun basicStorageTest() {
        TestStorage["1"] = "1"
        assert(TestStorage.get<String>("1") == "1")
        assert(TestStorage.map.isNotEmpty())

        TestStorage["2"] = 2
        assert(TestStorage.get<Int>("2") == 2)

        TestStorage["3"] = TestBundle(test = true)
        assert(TestStorage.get<TestBundle>("3")!!.name == "Default")
        assert(TestStorage.get<TestBundle>("3")!!.test)
        TestStorage["3"] = null
        val mustNull: String? = TestStorage["3"]
        assert(mustNull == null)
        assert(TestStorage.get<TestBundle>("3") == null)
    }

    @Test
    fun mutableStateTest() = runComposeUiTest {
        setContent {
            var nullable by TestStorage.keyOf<String>("1")
            val nonnull = TestStorage.keyOf("2") { "nonNull" }
            Text(
                text = "$nullable ${nonnull.value}",
                modifier = Modifier.testTag("text")
            )
            Button(
                onClick = {
                    nullable = "filled"
                    TestStorage[nonnull.key] = "received"
                },
                modifier = Modifier.testTag("button")
            ) {}
        }
        onNodeWithTag("text").assertTextEquals("null nonNull")
        onNodeWithTag("button").performClick()
        onNodeWithTag("text").assertTextEquals("filled received")
    }

    @Test
    fun structuralKeyOfTest() {
        val localUser = object : KVStorage.KeyOf("Settings", "User"),
            StorageProvider by TestStorage {
            var name = keyOf<String>("nickname") { "Unnamed" }
            var age by keyOf<Int>("age")
            val accounts = object : Nesting("account") {
                var github by keyOf("github") { "ghost" }
                var bilibili by keyOf<String>("bilibili")
            }
        }

        assert(localUser.name.value == "Unnamed")
        localUser.name.value = "Kawulf"
        assert(localUser.name.value == "Kawulf")
        localUser.name.clear()
        assert(localUser.name.value == "Unnamed")

        assert(localUser.age == null)
        localUser.age = 15
        assert(localUser.age == 15)
        assert(TestStorage.map[KVStorage.resolve(*localUser.keys, "age")] == StorageSerialization.encode(Int::class, 15))

        assert(localUser.accounts.github == "ghost")
        localUser.accounts.github = "Cdm2883"
        assert(localUser.accounts.github == "Cdm2883")
        assert(TestStorage.map[localUser.accounts.resolve("github")] == StorageSerialization.encode(String::class, "Cdm2883"))

        assert(localUser.accounts.bilibili == null)
        localUser.accounts.bilibili = "Cdm2883"
        assert(localUser.accounts.bilibili == "Cdm2883")
        assert(TestStorage.map[KVStorage.resolve("Settings", "User", "account", "bilibili")] == StorageSerialization.encode(String::class, "Cdm2883"))

        var outerProperty by localUser.keyOf<Boolean>("outer")
        outerProperty = true
        assert(outerProperty!!)
        assert(TestStorage.map[localUser.resolve("outer")] == StorageSerialization.encode(Boolean::class, true))

        val extendProperties = object : KVStorage.KeyOf(localUser), StorageProvider by localUser {
            var favoriteLanguage by keyOf<String>("language") { "TS" }
        }
        assert(extendProperties.favoriteLanguage == "TS")
        extendProperties.favoriteLanguage = "Kotlin"
        assert(extendProperties.favoriteLanguage == "Kotlin")
        assert(TestStorage.map[localUser.resolve("language")] == StorageSerialization.encode(String::class, "Kotlin"))
    }

}
