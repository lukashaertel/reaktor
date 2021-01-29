package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import eu.metatools.reaktor.gdx.data.Ref
import eu.metatools.reaktor.gdx.internals.extAlign
import eu.metatools.reaktor.gdx.internals.extScaling

open class VImage(
    val drawable: Drawable? = defaultDrawable,
    val scaling: Scaling = defaultScaling,
    val align: Int = defaultAlign,
    fillParent: Boolean = defaultFillParent,
    layoutEnabled: Boolean = defaultLayoutEnabled,
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
    ref: Ref? = defaultRef
) : VWidget<Image>(
    fillParent,
    layoutEnabled,
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
        val defaultDrawable: Drawable? = null
        val defaultScaling = Scaling.stretch
        val defaultAlign = Align.center

        private const val ownProps = 3
    }

    override fun create() = Image(drawable, scaling, align)

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> drawable
        1 -> scaling
        2 -> align
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: Image): Any? = when (prop) {
        0 -> actual.drawable
        1 -> actual.extScaling
        2 -> actual.extAlign
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: Image, value: Any?) {
        when (prop) {
            0 -> actual.drawable = value as Drawable?
            1 -> actual.setScaling(value as Scaling)
            2 -> actual.setAlign(value as Int)
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }
}