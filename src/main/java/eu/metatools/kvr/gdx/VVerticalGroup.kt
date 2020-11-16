package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import eu.metatools.kvr.gdx.data.Extents
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

data class VVerticalGroup(
    // VVerticalGroup.
    val round: Boolean,
    val reverse: Boolean,
    val space: Float,
    val wrapSpace: Float,
    val pad: Extents,
    val align: Int,
    val fill: Float,
    val wrap: Boolean,
    val expand: Boolean,
    val columnAlign: Int,

    // VWidgetGroup
    override val fillParent: Boolean,
    override val layoutEnabled: Boolean,

    // VGroup
    override val children: List<VActor<*>>,

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
    override val ref: Ref<VerticalGroup>? = null
) : VWidgetGroup<VerticalGroup>() {
    companion object {
        const val defaultRound = true
        const val defaultReverse = false
        const val defaultSpace = 0f
        const val defaultWrapSpace = 0f
        val defaultPad = Extents.zero
        const val defaultAlign = Align.top
        const val defaultFill = 0f
        const val defaultWrap = false
        const val defaultExpand = false
        const val defaultColumnAlign = 0
        val defaultTouchable = Touchable.childrenOnly

        val round = hidden<VerticalGroup, Boolean>("round")
        val columnAlign = hidden<VerticalGroup, Int>("columnAlign")
    }

    override fun create() = VerticalGroup()

    override fun assign(actual: VerticalGroup) {
        actual.setRound(round)
        actual.reverse(reverse)
        actual.space(space)
        actual.wrapSpace(wrapSpace)
        actual.pad(pad.top, pad.left, pad.bottom, pad.right)
        actual.align(align)
        actual.fill(fill)
        actual.wrap(wrap)
        actual.expand(expand)
        actual.columnAlign(align)

        super.assign(actual)
    }

    override fun props() = 29

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
        9 -> columnAlign
        10 -> fillParent
        11 -> layoutEnabled
        12 -> children
        13 -> color
        14 -> name
        15 -> originX
        16 -> originY
        17 -> x
        18 -> y
        19 -> width
        20 -> height
        21 -> rotation
        22 -> scaleX
        23 -> scaleY
        24 -> visible
        25 -> debug
        26 -> touchable
        27 -> listeners
        28 -> captureListeners

        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: VerticalGroup): Any? = when (prop) {
        0 -> round(actual)
        1 -> actual.reverse
        2 -> actual.space
        3 -> actual.wrapSpace
        4 -> Extents(actual.padTop, actual.padLeft, actual.padBottom, actual.padRight)
        5 -> actual.align
        6 -> actual.fill
        7 -> actual.wrap
        8 -> actual.expand
        9 -> columnAlign(actual)
        10 -> fillParent(actual)
        11 -> layoutEnabled(actual)
        12 -> wrapChildren(prop, actual)
        13 -> actual.color
        14 -> actual.name
        15 -> actual.originX
        16 -> actual.originY
        17 -> actual.x
        18 -> actual.y
        19 -> actual.width
        20 -> actual.height
        21 -> actual.rotation
        22 -> actual.scaleX
        23 -> actual.scaleY
        24 -> actual.isVisible
        25 -> actual.debug
        26 -> actual.touchable
        27 -> wrapListeners(prop, actual.listeners)
        28 -> wrapListeners(prop, actual.captureListeners)

        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: VerticalGroup, value: Any?) {
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
            9 -> actual.columnAlign(value as Int)
            10 -> actual.setFillParent(value as Boolean)
            11 -> actual.setLayoutEnabled(value as Boolean)
            12 -> updateActualChildren()
            13 -> actual.color = value as Color
            14 -> actual.name = value as String?
            15 -> actual.originX = value as Float
            16 -> actual.originY = value as Float
            17 -> actual.x = value as Float
            18 -> actual.y = value as Float
            19 -> actual.width = value as Float
            20 -> actual.height = value as Float
            21 -> actual.rotation = value as Float
            22 -> actual.scaleX = value as Float
            23 -> actual.scaleY = value as Float
            24 -> actual.isVisible = value as Boolean
            25 -> actual.debug = value as Boolean
            26 -> actual.touchable = value as Touchable
            27 -> throw UnsupportedOperationException()
            28 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
        }
    }
}

inline fun verticalGroup(
    // VVerticalGroup.
    round: Boolean = VVerticalGroup.defaultRound,
    reverse: Boolean = VVerticalGroup.defaultReverse,
    space: Float = VVerticalGroup.defaultSpace,
    wrapSpace: Float = VVerticalGroup.defaultWrapSpace,
    pad: Extents = VVerticalGroup.defaultPad,
    align: Int = VVerticalGroup.defaultAlign,
    fill: Float = VVerticalGroup.defaultFill,
    wrap: Boolean = VVerticalGroup.defaultWrap,
    expand: Boolean = VVerticalGroup.defaultExpand,
    columnAlign: Int = VVerticalGroup.defaultColumnAlign,

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
    touchable: Touchable = VVerticalGroup.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,

    // VRef
    @Suppress("unchecked_cast")
    ref: Ref<VerticalGroup>? = VRef.defaultRef as Ref<VerticalGroup>?,

    // VGroup
    children: () -> Unit
) = constructParent<VActor<*>, VVerticalGroup>(children) {
    VVerticalGroup(
        round,
        reverse,
        space,
        wrapSpace,
        pad,
        align,
        fill,
        wrap,
        expand,
        columnAlign,
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