package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.DelayedRemovalArray
import eu.metatools.kvr.Delegation
import eu.metatools.kvr.gdx.utils.EventMediator
import eu.metatools.kvr.gdx.utils.resumeEventMediators
import eu.metatools.kvr.gdx.utils.suspendEventMediators

abstract class VActor<A : Actor> : VRef<A>() {
    companion object {
        val defaultColor: Color = Color.WHITE
        val defaultName: String? = null
        const val defaultOriginX = 0f
        const val defaultOriginY = 0f
        const val defaultX = 0f
        const val defaultY = 0f
        const val defaultWidth = 0f
        const val defaultHeight = 0f
        const val defaultRotation = 0f
        const val defaultScaleX = 1f
        const val defaultScaleY = 1f
        const val defaultVisible = true
        const val defaultDebug = false
        val defaultTouchable = Touchable.enabled
        val defaultListeners = listOf<EventListener>()
        val defaultCaptureListeners = listOf<EventListener>()
    }

    abstract val color: Color
    abstract val name: String?
    abstract val originX: Float
    abstract val originY: Float
    abstract val x: Float
    abstract val y: Float
    abstract val width: Float
    abstract val height: Float
    abstract val rotation: Float
    abstract val scaleX: Float
    abstract val scaleY: Float
    abstract val visible: Boolean
    abstract val debug: Boolean
    abstract val touchable: Touchable

    abstract val listeners: List<EventListener>
    abstract val captureListeners: List<EventListener>

    override fun assign(actual: A) {
        actual.color = color
        actual.name = name
        actual.originX = originX
        actual.originY = originY
        actual.x = x
        actual.y = y
        actual.width = width
        actual.height = height
        actual.rotation = rotation
        actual.scaleX = scaleX
        actual.scaleY = scaleY
        actual.isVisible = visible
        actual.debug = debug
        actual.touchable = touchable
        for (listener in listeners)
            actual.listeners.add(EventMediator(listener))
        for (captureListener in captureListeners)
            actual.captureListeners.add(EventMediator(captureListener))

        super.assign(actual)
    }

    override fun begin(actual: A) {
        super.begin(actual)
        actual.listeners.suspendEventMediators()
    }

    override fun end(actual: A) {
        actual.listeners.resumeEventMediators()
        super.end(actual)
    }
}

fun wrapListeners(prop: Int, listeners: DelayedRemovalArray<EventListener>) = Delegation.list(listeners, prop,
    { count { it is EventMediator } },
    { at -> filterIsInstance<EventMediator>()[at].handler },
    { at, value: EventListener -> filterIsInstance<EventMediator>()[at].handler = value },
    { value: EventListener -> add(EventMediator(value)) },
    { at -> removeValue(filterIsInstance<EventMediator>()[at], true) }
)
