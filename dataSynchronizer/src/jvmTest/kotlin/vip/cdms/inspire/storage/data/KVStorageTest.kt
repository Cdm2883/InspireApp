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

}
