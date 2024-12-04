package vip.cdms.inspire.whiteboard.utils.jpen

import jpen.PenManager
import jpen.event.PenListener
import jpen.owner.AbstractPenOwner
import jpen.owner.PenClip
import java.awt.Point
import java.awt.geom.Point2D

/**
 * Use JPen without AWT (relative to screen).
 *
 * See [JPen #10 - provide simple non-AWT example](https://github.com/nicarran/jpen/issues/10).
 */
@Suppress("MemberVisibilityCanBePrivate")
object RegularJPen {
    init {
        JPenUtils.loadNativeLibrary()
    }

    private object Clip : PenClip {  // do nothing, process when needed
        override fun evalLocationOnScreen(pointOnScreen: Point) {}
        override operator fun contains(point: Point2D.Float) = true
    }
    private object Owner : AbstractPenOwner() {
        override fun getPenProviderConstructors() = JPenUtils.getPenOnlyProviderConstructors()
        override fun getPenClip() = Clip
        override fun draggingOutDisengaged() {}
        override fun init() { penManagerHandle.setPenManagerPaused(false) }
    }
    @JvmStatic
    val Manager = PenManager(Owner)

    @JvmStatic
    fun addListener(listener: PenListener) = Manager.pen.addListener(listener)
}
