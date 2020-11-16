package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.maltaisn.msdfgdx.FontStyle
import com.maltaisn.msdfgdx.MsdfFont
import com.maltaisn.msdfgdx.MsdfShader
import com.maltaisn.msdfgdx.widget.MsdfLabel
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.internals.extShader
import eu.metatools.kvr.gdx.internals.extSkin
import eu.metatools.kvr.gdx.internals.extWrap

open class VMsdfLabel(
    val text: CharSequence?,
    val shader: MsdfShader,
    val font: MsdfFont,
    val fontStyle: FontStyle,
    val labelAlign: Int,
    val lineAlign: Int,
    val wrap: Boolean,
    val disabled: Boolean,
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
    ref: Ref<MsdfLabel>?
) : VWidget<MsdfLabel>(
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
        val defaultFontStyle = FontStyle()
        const val defaultLabelAlign = Align.left
        const val defaultLineAlign = Align.left
        const val defaultWrap = false
        const val defaultDisabled = false

        private const val ownProps = 8
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

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> text
        1 -> shader
        2 -> font
        3 -> fontStyle
        4 -> labelAlign
        5 -> lineAlign
        6 -> wrap
        7 -> disabled
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: MsdfLabel): Any? = when (prop) {
        0 -> actual.text
        1 -> actual.extShader
        2 -> actual.extSkin["default", MsdfFont::class.java]
        3 -> actual.fontStyle
        4 -> actual.labelAlign
        5 -> actual.lineAlign
        6 -> actual.extWrap
        7 -> actual.isDisabled
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: MsdfLabel, value: Any?) {
        when (prop) {
            0 -> actual.setText(value as CharSequence?)
            1 -> {
                // Update shader in skin and update field.
                actual.extSkin.add("default", value as MsdfShader)
                actual.extShader = value
            }
            2 -> {
                // Update font in skin and reload by assigning font style.
                actual.extSkin.add("default", value as MsdfFont)
                actual.fontStyle = actual.fontStyle
            }
            3 -> actual.fontStyle = value as FontStyle
            4 -> actual.setAlignment(value as Int, actual.lineAlign)
            5 -> actual.setAlignment(labelAlign, value as Int)
            6 -> actual.setWrap(value as Boolean)
            7 -> actual.isDisabled = value as Boolean
            else -> super.updateActual(prop - ownProps, actual, value)
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