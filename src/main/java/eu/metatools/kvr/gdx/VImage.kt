package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

data class VImage(
    // VImage
    val drawable: Drawable?,
    val scaling: Scaling,
    val align: Int,

    // VWidget
    override val fillParent: Boolean,
    override val layoutEnabled: Boolean,

    // VActor
    override val color: Color,
    override val name: String?,
    override val originX: Float,
    override val originY: Float,
    override val x: Float,
    override val y: Float,
    override val width: Float,
    override val height: Float,
    override val rotation: Float,
    override val scaleX: Float,
    override val scaleY: Float,
    override val visible: Boolean,
    override val debug: Boolean,
    override val touchable: Touchable,
    override val listeners: List<EventListener>,
    override val captureListeners: List<EventListener>,

    // VRef
    override val ref: Ref<Image>? = null
) : VWidget<Image>() {
    companion object {
        val defaultDrawable: Drawable? = null
        val defaultScaling = Scaling.stretch
        val defaultAlign = Align.center
        private val scaling = hidden<Image, Scaling>("scaling")
        private val align = hidden<Image, Int>("align")
    }

    override fun create() = Image(drawable, scaling, align)

    override fun props() = 21

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> drawable
        1 -> scaling
        2 -> align
        3 -> fillParent
        4 -> layoutEnabled
        5 -> color
        6 -> name
        7 -> originX
        8 -> originY
        9 -> x
        10 -> y
        11 -> width
        12 -> height
        13 -> rotation
        14 -> scaleX
        15 -> scaleY
        16 -> visible
        17 -> debug
        18 -> touchable
        19 -> listeners
        20 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: Image): Any? = when (prop) {
        0 -> actual.drawable
        1 -> scaling(actual)
        2 -> align(actual)
        3 -> fillParent(actual)
        4 -> layoutEnabled(actual)
        5 -> actual.color
        6 -> actual.name
        7 -> actual.originX
        8 -> actual.originY
        9 -> actual.x
        10 -> actual.y
        11 -> actual.width
        12 -> actual.height
        13 -> actual.rotation
        14 -> actual.scaleX
        15 -> actual.scaleY
        16 -> actual.isVisible
        17 -> actual.debug
        18 -> actual.touchable
        19 -> wrapListeners(prop, actual.listeners)
        20 -> wrapListeners(prop, actual.captureListeners)

        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: Image, value: Any?) {
        when (prop) {
            0 -> actual.drawable = value as Drawable?
            1 -> actual.setScaling(value as Scaling)
            2 -> actual.setAlign(value as Int)
            3 -> actual.setFillParent(value as Boolean)
            4 -> actual.setLayoutEnabled(value as Boolean)
            5 -> actual.color = value as Color
            6 -> actual.name = value as String?
            7 -> actual.originX = value as Float
            8 -> actual.originY = value as Float
            9 -> actual.x = value as Float
            10 -> actual.y = value as Float
            11 -> actual.width = value as Float
            12 -> actual.height = value as Float
            13 -> actual.rotation = value as Float
            14 -> actual.scaleX = value as Float
            15 -> actual.scaleY = value as Float
            16 -> actual.isVisible = value as Boolean
            17 -> actual.debug = value as Boolean
            18 -> actual.touchable = value as Touchable
            19 -> throw UnsupportedOperationException()
            20 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
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
    ref: Ref<Image>? = null
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