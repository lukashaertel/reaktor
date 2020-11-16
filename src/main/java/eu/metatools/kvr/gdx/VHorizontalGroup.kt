package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.utils.Align
import eu.metatools.kvr.gdx.data.Extents
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.internals.extRound
import eu.metatools.kvr.gdx.internals.extRowAlign

open class VHorizontalGroup(
    val round: Boolean,
    val reverse: Boolean,
    val space: Float,
    val wrapSpace: Float,
    val pad: Extents,
    val align: Int,
    val fill: Float,
    val expand: Boolean,
    val wrap: Boolean,
    val rowAlign: Int,
    fillParent: Boolean,
    layoutEnabled: Boolean,
    children: List<VActor<*>>,
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
    ref: Ref<HorizontalGroup>? = null
) : VWidgetGroup<HorizontalGroup>(
    fillParent,
    layoutEnabled,
    children,
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
        const val defaultRound = true
        const val defaultReverse = false
        const val defaultSpace = 0f
        const val defaultWrapSpace = 0f
        val defaultPad = Extents.zero
        const val defaultAlign = Align.left
        const val defaultFill = 0f
        const val defaultWrap = false
        const val defaultExpand = false
        const val defaultRowAlign = 0
        val defaultTouchable = Touchable.childrenOnly


        private const val ownProps = 10
    }

    override fun create() = HorizontalGroup()

    override fun assign(actual: HorizontalGroup) {
        actual.setRound(round)
        actual.reverse(reverse)
        actual.space(space)
        actual.wrapSpace(wrapSpace)
        actual.pad(pad.top, pad.left, pad.bottom, pad.right)
        actual.align(align)
        actual.fill(fill)
        actual.wrap(wrap)
        actual.expand(expand)
        actual.rowAlign(align)

        super.assign(actual)
    }

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> round
        1 -> reverse
        2 -> space
        3 -> wrapSpace
        4 -> pad
        5 -> align
        6 -> fill
        7 -> wrap
        8 -> expand
        9 -> rowAlign
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: HorizontalGroup): Any? = when (prop) {
        0 -> actual.extRound
        1 -> actual.reverse
        2 -> actual.space
        3 -> actual.wrapSpace
        4 -> Extents(actual.padTop, actual.padLeft, actual.padBottom, actual.padRight)
        5 -> actual.align
        6 -> actual.fill
        7 -> actual.wrap
        8 -> actual.expand
        9 -> actual.extRowAlign
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: HorizontalGroup, value: Any?) {
        when (prop) {
            0 -> actual.setRound(value as Boolean)
            1 -> actual.reverse(value as Boolean)
            2 -> actual.space(value as Float)
            3 -> actual.wrapSpace(value as Float)
            4 -> actual.pad((value as Extents).top, value.left, value.bottom, value.right)
            5 -> actual.align(value as Int)
            6 -> actual.fill(value as Float)
            7 -> actual.wrap(value as Boolean)
            8 -> actual.expand(value as Boolean)
            9 -> actual.rowAlign(value as Int)
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }
}

inline fun horizontalGroup(
    // VHorizontalGroup.
    round: Boolean = VHorizontalGroup.defaultRound,
    reverse: Boolean = VHorizontalGroup.defaultReverse,
    space: Float = VHorizontalGroup.defaultSpace,
    wrapSpace: Float = VHorizontalGroup.defaultWrapSpace,
    pad: Extents = VHorizontalGroup.defaultPad,
    align: Int = VHorizontalGroup.defaultAlign,
    fill: Float = VHorizontalGroup.defaultFill,
    wrap: Boolean = VHorizontalGroup.defaultWrap,
    expand: Boolean = VHorizontalGroup.defaultExpand,
    rowAlign: Int = VHorizontalGroup.defaultRowAlign,

    // VWidgetGroup
    fillParent: Boolean = VWidgetGroup.defaultFillParent,
    layoutEnabled: Boolean = VWidgetGroup.defaultLayoutEnabled,

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
    touchable: Touchable = VHorizontalGroup.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,

    // VRef
    @Suppress("unchecked_cast")
    ref: Ref<HorizontalGroup>? = VRef.defaultRef as Ref<HorizontalGroup>?,

    // VGroup
    children: () -> Unit
) = constructParent<VActor<*>, VHorizontalGroup>(children) {
    VHorizontalGroup(
        round,
        reverse,
        space,
        wrapSpace,
        pad,
        align,
        fill,
        wrap,
        expand,
        rowAlign,
        fillParent,
        layoutEnabled,
        it,
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