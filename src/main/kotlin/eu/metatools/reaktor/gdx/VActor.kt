package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.DelayedRemovalArray
import eu.metatools.reaktor.Delegation
import eu.metatools.reaktor.gdx.utils.EventMediator
import eu.metatools.reaktor.gdx.utils.resumeEventMediators
import eu.metatools.reaktor.gdx.utils.suspendEventMediators

abstract class VActor<A : Actor>(
    val color: Color = defaultColor,
    val name: String? = defaultName,
    val originX: Float = defaultOriginX,
    val originY: Float = defaultOriginY,
    val x: Float = defaultX,
    val y: Float = defaultY,
    val width: Float = defaultWidth,
    val height: Float = defaultHeight,
    val rotation: Float = defaultRotation,
    val scaleX: Float = defaultScaleX,
    val scaleY: Float = defaultScaleY,
    val visible: Boolean = defaultVisible,
    val debug: Boolean = defaultDebug,
    val touchable: Touchable = defaultTouchable,
    val listeners: List<EventListener> = defaultListeners,
    val captureListeners: List<EventListener> = defaultCaptureListeners,
    ref: (A) -> Unit = defaultRef,
) : VRef<A>(ref) {
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

        private const val ownProps = 16
    }

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

    override val props = ownProps

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> color
        1 -> name
        2 -> originX
        3 -> originY
        4 -> x
        5 -> y
        6 -> width
        7 -> height
        8 -> rotation
        9 -> scaleX
        10 -> scaleY
        11 -> visible
        12 -> debug
        13 -> touchable
        14 -> listeners
        15 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: A): Any? = when (prop) {
        0 -> actual.color
        1 -> actual.name
        2 -> actual.originX
        3 -> actual.originY
        4 -> actual.x
        5 -> actual.y
        6 -> actual.width
        7 -> actual.height
        8 -> actual.rotation
        9 -> actual.scaleX
        10 -> actual.scaleY
        11 -> actual.isVisible
        12 -> actual.debug
        13 -> actual.touchable
        14 -> wrapListeners(prop, actual.listeners)
        15 -> wrapListeners(prop, actual.captureListeners)
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: A, value: Any?) {
        when (prop) {
            0 -> actual.color = value as Color
            1 -> actual.name = value as String?
            2 -> actual.originX = value as Float
            3 -> actual.originY = value as Float
            4 -> actual.x = value as Float
            5 -> actual.y = value as Float
            6 -> actual.width = value as Float
            7 -> actual.height = value as Float
            8 -> actual.rotation = value as Float
            9 -> actual.scaleX = value as Float
            10 -> actual.scaleY = value as Float
            11 -> actual.isVisible = value as Boolean
            12 -> actual.debug = value as Boolean
            13 -> actual.touchable = value as Touchable
            14 -> throw UnsupportedOperationException()
            15 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
        }
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
