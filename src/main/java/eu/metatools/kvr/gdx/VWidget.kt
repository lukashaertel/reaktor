package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.internals.extFillParent
import eu.metatools.kvr.gdx.internals.extLayoutEnabled

abstract class VWidget<A : Widget>(
    val fillParent: Boolean,
    val layoutEnabled: Boolean,
    color: Color,
    name: String?,
    originX: Float,
    originY: Float,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    rotation: Float,
    scaleX: Float,
    scaleY: Float,
    visible: Boolean,
    debug: Boolean,
    touchable: Touchable,
    listeners: List<EventListener>,
    captureListeners: List<EventListener>,
    ref: Ref<A>?
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
    ref
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