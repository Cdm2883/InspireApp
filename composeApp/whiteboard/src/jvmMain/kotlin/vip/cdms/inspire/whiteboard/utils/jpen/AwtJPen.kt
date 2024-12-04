package vip.cdms.inspire.whiteboard.utils.jpen

import jpen.PenProvider
import jpen.internal.ActiveWindowProperty
import jpen.owner.AbstractPenOwner
import jpen.owner.PenClip
import java.awt.Component
import java.awt.Dialog
import java.awt.Point
import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.geom.Point2D
import java.lang.ref.WeakReference
import javax.swing.SwingUtilities
import kotlin.concurrent.Volatile

// the original one is final...
/**
 * ```kt
 * val listener = object : PenAdapter() { ... }
 * PenManager(AwtJPen.Owner(window)).pen.addListener(listener)
 * ```
 */
object AwtJPen {
    class ComponentClip(private val owner: ComponentOwner) : PenClip {
        override fun evalLocationOnScreen(pointOnScreen: Point) {
            pointOnScreen.apply { y = 0; x = y }
            SwingUtilities.convertPointToScreen(pointOnScreen, owner.activeComponent)
        }
        override fun contains(point: Point2D.Float) = owner.activeComponent.let {
            !(point.x < 0.0f) && !(point.y < 0.0f) && !(point.x > it.width.toFloat()) && !(point.y > it.height.toFloat())
        }
    }

    abstract class ComponentOwner(private val penProviderConstructors: List<PenProvider. Constructor>) : AbstractPenOwner() {
        abstract val activeComponent: Component
        private val _penClip = ComponentClip(@Suppress("LeakingThis") this)
        override fun getPenProviderConstructors() = penProviderConstructors
        override fun getPenClip() = _penClip
        override fun draggingOutDisengaged() = pause()

        protected fun pause() {
            unpauser.enabled = false
            activeWindowPL.enabled = false
            penManagerHandle.setPenManagerPaused(true)
        }
        protected open fun getPenSchedulerLock(component: Component?): Any = if (component != null && Thread.holdsLock(component.treeLock))
            throw AssertionError("Tried to hold penSchedulerLock while holding Component's treeLock")
        else penManagerHandle.penSchedulerLock

        private val activeWindowPL = ActiveWindowPL()
        private inner class ActiveWindowPL : ActiveWindowProperty.Listener {
            private val activeWindowP by lazy { ActiveWindowProperty(this) }
            var enabled = false
                get() = field.also { activeWindowP }

            override fun activeWindowChanged(activeWindow: Window?) = if (enabled) synchronized(getPenSchedulerLock(activeWindow)) {
                when {
                    activeWindow == null -> pauseAMoment()
                    activeWindow is Dialog && activeWindow.isModal && activeWindow !== SwingUtilities.getWindowAncestor(activeComponent) -> pauseAMoment()
                }
            } else Unit

            private fun pauseAMoment() { pause(); unpauser.enabled = true }
        }

        protected val unpauser = Unpauser()
        protected inner class Unpauser : MouseMotionListener {
            private var mActiveComponentRef: WeakReference<Component>? = null
            @Volatile
            @set:Synchronized
            var enabled = false
                set(value) {
                    if (value != field) if (value) mActiveComponentRef = WeakReference(activeComponent.apply { addMouseMotionListener(unpauser) })
                    else mActiveComponentRef?.get()?.removeMouseMotionListener(unpauser).also { mActiveComponentRef = null }
                    field = value
                }

            override fun mouseMoved(ev: MouseEvent) { unpause() }
            override fun mouseDragged(ev: MouseEvent) {}

            fun unpause() = if (enabled) synchronized(penManagerHandle.penSchedulerLock) {
                activeWindowPL.enabled = false
                penManagerHandle.setPenManagerPaused(false)
                enabled = false
            } else Unit
        }
    }

    class Owner(
        override val activeComponent: Component,
        penProviderConstructors: List<PenProvider. Constructor> = JPenUtils.getPenOnlyProviderConstructors()
    ) : ComponentOwner(penProviderConstructors) {
        private val mouseListener = object : MouseAdapter() {
            override fun mouseExited(ev: MouseEvent) = synchronized(getPenSchedulerLock(ev.component)) {
                if (!startDraggingOut()) pause()
            }
            override fun mouseEntered(ev: MouseEvent) = synchronized(getPenSchedulerLock(ev.component)) {
                if (!stopDraggingOut()) unpauser.enabled = true
            }
        }
        override fun init() = activeComponent.addMouseListener(mouseListener)
    }
}
