package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

data class VLabel(
    val text: String,
    val style: LabelStyle,

    val fontScaleX: Float,
    val fontScaleY: Float,
    val labelAlign: Int,
    val lineAlign: Int,
    val wrap: Boolean,

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
    override val ref: Ref<Label>?
) : VWidget<Label>() {
    companion object {
        const val defaultFontScaleX: Float = 1f
        const val defaultFontScaleY: Float = 1f
        const val defaultLabelAlign: Int = Align.left
        const val defaultLineAlign: Int = Align.left
        const val defaultWrap: Boolean = false

        private val wrap = hidden<Label, Boolean>("wrap")
    }

    override fun create() = Label(text, style)

    override fun assign(actual: Label) {
        actual.setFontScale(fontScaleX, fontScaleY)
        actual.setAlignment(labelAlign, lineAlign)
        actual.setWrap(wrap)
        super.assign(actual)
    }

    override fun props() = 25

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> text
        1 -> style
        2 -> fontScaleX
        3 -> fontScaleY
        4 -> labelAlign
        5 -> lineAlign
        6 -> wrap
        7 -> fillParent
        8 -> layoutEnabled
        9 -> color
        10 -> name
        11 -> originX
        12 -> originY
        13 -> x
        14 -> y
        15 -> width
        16 -> height
        17 -> rotation
        18 -> scaleX
        19 -> scaleY
        20 -> visible
        21 -> debug
        22 -> touchable
        23 -> listeners
        24 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: Label): Any? = when (prop) {
        0 -> actual.text
        1 -> actual.style
        2 -> actual.fontScaleX
        3 -> actual.fontScaleY
        4 -> actual.labelAlign
        5 -> actual.lineAlign
        6 -> wrap(actual)
        7 -> fillParent(actual)
        8 -> layoutEnabled(actual)
        9 -> actual.color
        10 -> actual.name
        11 -> actual.originX
        12 -> actual.originY
        13 -> actual.x
        14 -> actual.y
        15 -> actual.width
        16 -> actual.height
        17 -> actual.rotation
        18 -> actual.scaleX
        19 -> actual.scaleY
        20 -> actual.isVisible
        21 -> actual.debug
        22 -> actual.touchable
        23 -> wrapListeners(prop, actual.listeners)
        24 -> wrapListeners(prop, actual.captureListeners)
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: Label, value: Any?) {
        when (prop) {
            0 -> actual.setText(value as String)
            1 -> actual.style = value as LabelStyle
            2 -> actual.setFontScale(value as Float, actual.fontScaleY)
            3 -> actual.setFontScale(actual.fontScaleX, value as Float)
            4 -> actual.setAlignment(value as Int, actual.lineAlign)
            5 -> actual.setAlignment(labelAlign, value as Int)
            6 -> actual.setWrap(value as Boolean)
            7 -> actual.setFillParent(value as Boolean)
            8 -> actual.setLayoutEnabled(value as Boolean)
            9 -> actual.color = value as Color
            10 -> actual.name = value as String?
            11 -> actual.originX = value as Float
            12 -> actual.originY = value as Float
            13 -> actual.x = value as Float
            14 -> actual.y = value as Float
            15 -> actual.width = value as Float
            16 -> actual.height = value as Float
            17 -> actual.rotation = value as Float
            18 -> actual.scaleX = value as Float
            19 -> actual.scaleY = value as Float
            20 -> actual.isVisible = value as Boolean
            21 -> actual.debug = value as Boolean
            22 -> actual.touchable = value as Touchable
            23 -> throw UnsupportedOperationException()
            24 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
        }
    }
}

fun label(
    text: String,
    style: LabelStyle,
    fontScaleX: Float = VLabel.defaultFontScaleX,
    fontScaleY: Float = VLabel.defaultFontScaleY,
    labelAlign: Int = VLabel.defaultLabelAlign,
    lineAlign: Int = VLabel.defaultLineAlign,
    wrap: Boolean = VLabel.defaultWrap,

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
    @Suppress("unchecked_cast")
    ref: Ref<Label>? = VRef.defaultRef as Ref<Label>?
) = constructTerminal {
    VLabel(
        text,
        style,
        fontScaleX,
        fontScaleY,
        labelAlign,
        lineAlign,
        wrap,
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