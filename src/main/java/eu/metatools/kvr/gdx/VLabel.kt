package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

open class VLabel(
    val text: String,
    val style: LabelStyle,
    val fontScaleX: Float,
    val fontScaleY: Float,
    val labelAlign: Int,
    val lineAlign: Int,
    val wrap: Boolean,
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
    ref: Ref<Label>?
) : VWidget<Label>(fillParent,
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
    ref) {
    companion object {
        const val defaultFontScaleX: Float = 1f
        const val defaultFontScaleY: Float = 1f
        const val defaultLabelAlign: Int = Align.left
        const val defaultLineAlign: Int = Align.left
        const val defaultWrap: Boolean = false

        private val wrap = hidden<Label, Boolean>("wrap")

        private const val ownProps = 7
    }

    override fun create() = Label(text, style)

    override fun assign(actual: Label) {
        actual.setFontScale(fontScaleX, fontScaleY)
        actual.setAlignment(labelAlign, lineAlign)
        actual.setWrap(wrap)
        super.assign(actual)
    }

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> text
        1 -> style
        2 -> fontScaleX
        3 -> fontScaleY
        4 -> labelAlign
        5 -> lineAlign
        6 -> wrap
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: Label): Any? = when (prop) {
        0 -> actual.text
        1 -> actual.style
        2 -> actual.fontScaleX
        3 -> actual.fontScaleY
        4 -> actual.labelAlign
        5 -> actual.lineAlign
        6 -> wrap(actual)
        else -> super.getActual(prop - ownProps, actual)
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
            else -> super.updateActual(prop - ownProps, actual, value)
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