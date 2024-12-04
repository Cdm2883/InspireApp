package vip.cdms.inspire.whiteboard.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import vip.cdms.inspire.whiteboard.WhiteboardState

interface WhiteboardInitializer {
    companion object
}

val WhiteboardInitializer.Companion.Empty get() = object : WhiteboardInitializer {
}

val WhiteboardState.Companion.Saver get() = listSaver<WhiteboardState, Any>(
    save = { listOf() },
    restore = { WhiteboardState(WhiteboardInitializer.Empty) }
)

/** Creates a [WhiteboardState] that is remembered. */
@Composable
fun rememberWhiteboardState(initializer: WhiteboardInitializer.() -> Unit = {}) =
    rememberSaveable(initializer, saver = WhiteboardState.Saver) { WhiteboardState(WhiteboardInitializer.Empty.apply(initializer)) }
