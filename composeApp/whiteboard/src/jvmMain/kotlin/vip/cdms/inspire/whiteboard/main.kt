package vip.cdms.inspire.whiteboard

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jpen.*
import jpen.demo.StatusReport
import jpen.event.PenAdapter
import jpen.event.PenListener
import jpen.owner.AbstractPenOwner
import jpen.owner.PenClip
import jpen.provider.osx.CocoaProvider
import jpen.provider.wintab.WintabProvider
import jpen.provider.xinput.XinputProvider
import vip.cdms.inspire.utils.JvmUtils
import vip.cdms.inspire.whiteboard.utils.copyToJavaLibraryPath
import vip.cdms.inspire.whiteboard.utils.tempExtractInsideJar
import java.awt.Point
import kotlin.concurrent.thread

private class RegularPenClip : PenClip {
    override fun evalLocationOnScreen(pointOnScreen: Point) {}
    override operator fun contains(point: java.awt.geom.Point2D.Float) = true
}
private class RegularPenOwner : AbstractPenOwner() {
    override fun getPenProviderConstructors() = listOf<PenProvider.Constructor>(
//        jpen.provider.system.SystemProvider.Constructor(),  // only mouse & keyboard
        XinputProvider.Constructor(),
        WintabProvider.Constructor(),
        CocoaProvider.Constructor())
    private val penClip: RegularPenClip = RegularPenClip()
    override fun getPenClip() = penClip
    override fun draggingOutDisengaged() {}
    override fun init() {
        penManagerHandle.setPenManagerPaused(false)
    }
}
private class JPenExample(penListener: PenListener) {
    private val penOwner = RegularPenOwner()
    private val penManager = PenManager(penOwner)
    init {
        println(StatusReport(penManager))
        penManager.pen.addListener(penListener)
    }
}

fun loadJPenNativeLibrary() = listOf(
    "libjpen-2-4.so",
    "libjpen-2-4-x86_64.so",
    "jpen-2-3.dll",
    "jpen-2-3-64.dll",
    "libjpen-2-3.jnilib",
).forEach { lib ->
    runCatching {
        val file = JvmUtils.tempExtractInsideJar("jpen/$lib")
        JvmUtils.copyToJavaLibraryPath(file)
    }
}

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

            loadJPenNativeLibrary()

            thread {
                JPenExample(object : PenAdapter() {
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

                        for (levelType in PLevel.Type.VALUES){
                            val value = _pen.getLevelValue(levelType)
                            messages += "LEVEL type: ${levelType}, value: $value"
                        }

                        // only process tocks after an event happens
                        pen = null
                    }
                })
            }

        }
    }
}
