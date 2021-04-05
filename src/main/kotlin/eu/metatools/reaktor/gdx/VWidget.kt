package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import eu.metatools.reaktor.ex.consumeKey
import eu.metatools.reaktor.gdx.internals.extFillParent
import eu.metatools.reaktor.gdx.internals.extLayoutEnabled

abstract class VWidget<A : Widget> internal constructor(
    val fillParent: Boolean = defaultFillParent,
    val layoutEnabled: Boolean = defaultLayoutEnabled,
    color: Color = defaultColor,
    name: String? = defaultName,
    originX: Float = defaultOriginX,
    originY: Float = defaultOriginY,
    x: Float = defaultX,
    y: Float = defaultY,
    width: Float = defaultWidth,
    height: Float = defaultHeight,
    rotation: Float = defaultRotation,
    scaleX: Float = defaultScaleX,
    scaleY: Float = defaultScaleY,
    visible: Boolean = defaultVisible,
    debug: Boolean = defaultDebug,
    touchable: Touchable = defaultTouchable,
    listeners: List<EventListener> = defaultListeners,
    captureListeners: List<EventListener> = defaultCaptureListeners,
    ref: (A) -> Unit = defaultRef,
    key: Any? = consumeKey(),
) : VActor<A>(
    color,
    name,
    originX,
    originY,
    x,
    y,
    width,
    height,
    rotation,
    scaleX,
    scaleY,
    visible,
    debug,
    touchable,
    listeners,
    captureListeners,
    ref,
    key
) {
    companion object {
        const val defaultFillParent = false
        const val defaultLayoutEnabled = true

        private const val ownProps = 2
    }

    override fun assign(actual: A) {
        actual.setFillParent(fillParent)
        actual.setLayoutEnabled(layoutEnabled)
        super.assign(actual)
    }

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> fillParent
        1 -> layoutEnabled
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: A): Any? = when (prop) {
        0 -> actual.extFillParent
        1 -> actual.extLayoutEnabled
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: A, value: Any?) {
        when (prop) {
            0 -> actual.setFillParent(value as Boolean)
            1 -> actual.setLayoutEnabled(value as Boolean)
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }
}