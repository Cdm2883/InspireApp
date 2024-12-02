package vip.cdms.inspire.whiteboard.utils

import jpen.PenManager
import jpen.PenProvider
import jpen.event.PenListener
import jpen.owner.AbstractPenOwner
import jpen.owner.PenClip
import jpen.provider.osx.CocoaProvider
import jpen.provider.wintab.WintabProvider
import jpen.provider.xinput.XinputProvider
import vip.cdms.inspire.utils.JvmUtils
import java.awt.Point
import java.awt.geom.Point2D

@Suppress("MemberVisibilityCanBePrivate")
object RegularJPen {
    object Clip : PenClip {  // process when needed
        override fun evalLocationOnScreen(pointOnScreen: Point) {}
        override operator fun contains(point: Point2D.Float) = true
    }
    object Owner : AbstractPenOwner() {
        override fun getPenProviderConstructors() = listOf<PenProvider.Constructor>(
//            jpen.provider.system.SystemProvider.Constructor(),  // only mouse & keyboard
            XinputProvider.Constructor(),
            WintabProvider.Constructor(),
            CocoaProvider.Constructor())
        override fun getPenClip() = Clip
        override fun draggingOutDisengaged() {}
        override fun init() {
            penManagerHandle.setPenManagerPaused(false)
        }
    }
    @JvmStatic
    val Manager = PenManager(Owner)

    @JvmStatic
    fun addListener(listener: PenListener) = Manager.pen.addListener(listener)

    init { loadNativeLibrary() }
    @JvmStatic
    private var loadedNativeLibrary = false
    @JvmStatic
    fun loadNativeLibrary() = if (!loadedNativeLibrary) listOf(
        "libjpen-2-4.so",
        "libjpen-2-4-x86_64.so",
        "jpen-2-3.dll",
        "jpen-2-3-64.dll",
        "libjpen-2-3.jnilib",
    ).any { name ->  // build system will only package the native library of the current system
        runCatching {
            val lib = JvmUtils.tempExtractInsideJar("jpen/$name")
            JvmUtils.copyToJavaLibraryPath(lib)
        }.getOrNull() ?: false
    }.also { loadedNativeLibrary = it } else false
}
