package vip.cdms.inspire.storage.data

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import kotlin.test.Test

class KVStorageTest {

    object TestStorage : KVStorage() {
        val map = mutableMapOf<String, String?>()
        override fun setValue0(key: String, value: String?) { map[key] = value }
        override fun getValue0(key: String) = map[key]
    }

    @Test
    fun basicStorageTest() {
        TestStorage["1"] = "1"
        assert(TestStorage["1"] == "1")
        assert(TestStorage.map.isNotEmpty())
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
        assert(TestStorage.map["1"] == "filled")
        assert(TestStorage.map["2"] == "received")
    }

}
