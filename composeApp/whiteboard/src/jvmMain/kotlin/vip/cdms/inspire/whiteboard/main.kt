package vip.cdms.inspire.whiteboard

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jpen.PButton
import jpen.PLevel
import jpen.PLevelEvent
import jpen.Pen
import jpen.event.PenAdapter
import vip.cdms.inspire.whiteboard.utils.RegularJPen
import vip.cdms.inspire.whiteboard.utils.jpen.RegularJPen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Inspire Whiteboard",
    ) {
        val messages = remember { mutableStateListOf<String>() }

        SideEffect {
            (messages.size - 36).let { if (it > 0) messages.removeRange(0, it) }
        }
        BasicText(messages.joinToString("\n"))

        LaunchedEffect(Unit) {
            val listener = object : PenAdapter() {
                private var pen: Pen? = null

                override fun penLevelEvent(ev: PLevelEvent?) {
                    if (null == pen) pen = (ev ?: return).pen
                }

                override fun penTock(availableMillis: Long) {
                    messages += "penTock availableMillis: $availableMillis"
                    @Suppress("LocalVariableName") val _pen = pen ?: return

                    messages += "KIND ${_pen.kind}"

                    for (buttonType in PButton.Type.VALUES) {
                        if (_pen.getButtonValue(buttonType)) {
                            messages += "BUTTON_TYPE $buttonType"
                        }
                    }

                    for (levelType in PLevel.Type.VALUES) {
                        val value = _pen.getLevelValue(levelType)
                        messages += "LEVEL type: ${levelType}, value: $value"
                    }

                    // only process tocks after an event happens
                    pen = null
                }
            }
            RegularJPen.addListener(listener)
        }
    }
}
