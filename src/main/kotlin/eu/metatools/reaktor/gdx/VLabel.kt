package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import eu.metatools.reaktor.gdx.data.Ref
import eu.metatools.reaktor.gdx.internals.extWrap

open class VLabel(
    val text: String,
    val style: LabelStyle,
    val fontScaleX: Float = defaultFontScaleX,
    val fontScaleY: Float = defaultFontScaleY,
    val labelAlign: Int = defaultLabelAlign,
    val lineAlign: Int = defaultLineAlign,
    val wrap: Boolean = defaultWrap,
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
        6 -> actual.extWrap
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