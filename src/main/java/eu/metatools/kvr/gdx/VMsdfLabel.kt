package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.maltaisn.msdfgdx.FontStyle
import com.maltaisn.msdfgdx.MsdfFont
import com.maltaisn.msdfgdx.MsdfShader
import com.maltaisn.msdfgdx.widget.MsdfLabel
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

data class VMsdfLabel(
    val text: CharSequence?,
    val shader: MsdfShader,
    val font: MsdfFont,
    val fontStyle: FontStyle,
    val labelAlign: Int,
    val lineAlign: Int,
    val wrap: Boolean,
    val disabled: Boolean,

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
    override val ref: Ref<MsdfLabel>?
) : VWidget<MsdfLabel>() {
    companion object {
        val defaultFontStyle = FontStyle()
        const val defaultLabelAlign = Align.left
        const val defaultLineAlign = Align.left
        const val defaultWrap = false
        const val defaultDisabled = false


        private val shader = hidden<MsdfLabel, MsdfShader>("shader")
        private val skin = hidden<MsdfLabel, Skin>("skin")
        private val wrap = hidden<Label, Boolean>("wrap")
    }

    override fun create() = MsdfLabel(text, Skin().also {
        it.add("default", shader)
        it.add("default", font)
    }, fontStyle)

    override fun assign(actual: MsdfLabel) {
        actual.setAlignment(labelAlign, lineAlign)
        actual.setWrap(wrap)
        actual.isDisabled = disabled
        super.assign(actual)
    }

    override fun props() = 26

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> text
        1 -> shader
        2 -> font
        3 -> fontStyle
        4 -> labelAlign
        5 -> lineAlign
        6 -> wrap
        7 -> disabled
        8 -> fillParent
        9 -> layoutEnabled
        10 -> color
        11 -> name
        12 -> originX
        13 -> originY
        14 -> x
        15 -> y
        16 -> width
        17 -> height
        18 -> rotation
        19 -> scaleX
        20 -> scaleY
        21 -> visible
        22 -> debug
        23 -> touchable
        24 -> listeners
        25 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: MsdfLabel): Any? = when (prop) {
        0 -> actual.text
        1 -> shader(actual)
        2 -> skin(actual)["default", MsdfFont::class.java]
        3 -> actual.fontStyle
        4 -> actual.labelAlign
        5 -> actual.lineAlign
        6 -> wrap(actual)
        7 -> actual.isDisabled
        8 -> fillParent(actual)
        9 -> layoutEnabled(actual)
        10 -> actual.color
        11 -> actual.name
        12 -> actual.originX
        13 -> actual.originY
        14 -> actual.x
        15 -> actual.y
        16 -> actual.width
        17 -> actual.height
        18 -> actual.rotation
        19 -> actual.scaleX
        20 -> actual.scaleY
        21 -> actual.isVisible
        22 -> actual.debug
        23 -> actual.touchable
        24 -> wrapListeners(prop, actual.listeners)
        25 -> wrapListeners(prop, actual.captureListeners)
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: MsdfLabel, value: Any?) {
        when (prop) {
            0 -> actual.setText(value as CharSequence?)
            1 -> {
                // Update shader in skin and update field.
                skin(actual).add("default", value as MsdfShader)
                shader(actual, value)
            }
            2 -> {
                // Update font in skin and reload by assigning font style.
                skin(actual).add("default", value as MsdfFont)
                actual.fontStyle = actual.fontStyle
            }
            3 -> actual.fontStyle = value as FontStyle
            4 -> actual.setAlignment(value as Int, actual.lineAlign)
            5 -> actual.setAlignment(labelAlign, value as Int)
            6 -> actual.setWrap(value as Boolean)
            7 -> actual.isDisabled = value as Boolean
            8 -> actual.setFillParent(value as Boolean)
            9 -> actual.setLayoutEnabled(value as Boolean)
            10 -> actual.color = value as Color
            11 -> actual.name = value as String?
            12 -> actual.originX = value as Float
            13 -> actual.originY = value as Float
            14 -> actual.x = value as Float
            15 -> actual.y = value as Float
            16 -> actual.width = value as Float
            17 -> actual.height = value as Float
            18 -> actual.rotation = value as Float
            19 -> actual.scaleX = value as Float
            20 -> actual.scaleY = value as Float
            21 -> actual.isVisible = value as Boolean
            22 -> actual.debug = value as Boolean
            23 -> actual.touchable = value as Touchable
            24 -> throw UnsupportedOperationException()
            25 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
        }
    }
}

fun msdfLabel(
    text: CharSequence?,
    shader: MsdfShader,
    font: MsdfFont,
    fontStyle: FontStyle = VMsdfLabel.defaultFontStyle,
    labelAlign: Int = VMsdfLabel.defaultLabelAlign,
    lineAlign: Int = VMsdfLabel.defaultLineAlign,
    wrap: Boolean = VMsdfLabel.defaultWrap,
    disabled: Boolean = VMsdfLabel.defaultDisabled,

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
    ref: Ref<MsdfLabel>? = VRef.defaultRef as Ref<MsdfLabel>?
) = constructTerminal {
    VMsdfLabel(
        text,
        shader,
        font,
        fontStyle,
        labelAlign,
        lineAlign,
        wrap,
        disabled,
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