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
    val drawable: Drawable?,
    val scaling: Scaling,
    val align: Int,
    fillParent: Boolean,
    layoutEnabled: Boolean,
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
    ref: Ref? = null
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

fun image(
    // VImage
    drawable: Drawable? = VImage.defaultDrawable,
    scaling: Scaling = VImage.defaultScaling,
    align: Int = VImage.defaultAlign,

    // VWidget
    fillParent: Boolean = VWidget.defaultFillParent,
    layoutEnabled: Boolean = VWidget.defaultLayoutEnabled,

    // VActor
    color: Color = VActor.defaultColor,
    name: String? = VActor.defaultName,
    originX: Float = VActor.defaultOriginX,
    originY: Float = VActor.defaultOriginY,
    x: Float = VActor.defaultX,
    y: Float = VActor.defaultY,
    width: Float = VActor.defaultWidth,
    height: Float = VActor.defaultHeight,
    rotation: Float = VActor.defaultRotation,
    scaleX: Float = VActor.defaultScaleX,
    scaleY: Float = VActor.defaultScaleY,
    visible: Boolean = VActor.defaultVisible,
    debug: Boolean = VActor.defaultDebug,
    touchable: Touchable = VActor.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,

    // VRef
    ref: Ref? = VRef.defaultRef
) = constructTerminal {
    VImage(
        drawable,
        scaling,
        align,
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
    )
}