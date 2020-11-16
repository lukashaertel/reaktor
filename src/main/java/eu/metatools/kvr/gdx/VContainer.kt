package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import eu.metatools.kvr.gdx.data.ExtentValues
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

data class VContainer(
    val actor: VActor<*>,
    val minWidth: Value,
    val minHeight: Value,
    val prefWidth: Value,
    val prefHeight: Value,
    val maxWidth: Value,
    val maxHeight: Value,
    val fillX: Float,
    val fillY: Float,
    val align: Int,
    val background: Drawable?,
    val round: Boolean,
    val clip: Boolean,
    val pad: ExtentValues,

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
    override val ref: Ref<Container<Actor>>? = null
) : VWidgetGroup<Container<Actor>>() {
    companion object {
        val defaultMinWidth: Value = Value.minWidth
        val defaultMinHeight: Value = Value.minHeight
        val defaultPrefWidth: Value = Value.prefWidth
        val defaultPrefHeight: Value = Value.prefHeight
        val defaultMaxWidth: Value = Value.zero
        val defaultMaxHeight: Value = Value.zero
        const val defaultFillX: Float = 0f
        const val defaultFillY: Float = 0f
        const val defaultAlign: Int = 0
        val defaultBackground: Drawable? = null
        const val defaultRound: Boolean = true
        const val defaultClip: Boolean = false
        val defaultPad: ExtentValues = ExtentValues.zero
        val defaultTouchable = Touchable.childrenOnly

        private val fillX = hidden<Container<Actor>, Float>("fillX")
        private val fillY = hidden<Container<Actor>, Float>("fillY")
        private val round = hidden<Container<Actor>, Float>("round")
    }

    override fun create() = Container<Actor>()

    override fun assign(actual: Container<Actor>) {
        actual.minWidth(minWidth)
        actual.minHeight(minHeight)
        actual.prefWidth(prefWidth)
        actual.prefHeight(prefHeight)
        actual.maxWidth(maxWidth)
        actual.maxHeight(maxHeight)
        actual.fill(fillX, fillY)
        actual.align(align)
        actual.background(background)
        actual.setRound(round)
        actual.clip = clip
        actual.pad(pad.top, pad.left, pad.bottom, pad.right)
        actual.actor = actor.make()

        super.assign(actual)
    }

    override fun props() = 33

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> actor
        1 -> minWidth
        2 -> minHeight
        3 -> prefWidth
        4 -> prefHeight
        5 -> maxWidth
        6 -> maxHeight
        7 -> fillX
        8 -> fillY
        9 -> align
        10 -> background
        11 -> round
        12 -> clip
        13 -> pad
        14 -> fillParent
        15 -> layoutEnabled
        16 -> children
        17 -> color
        18 -> name
        19 -> originX
        20 -> originY
        21 -> x
        22 -> y
        23 -> width
        24 -> height
        25 -> rotation
        26 -> scaleX
        27 -> scaleY
        28 -> visible
        29 -> debug
        30 -> touchable
        31 -> listeners
        32 -> captureListeners

        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: Container<Actor>): Any? = when (prop) {
        0 -> actual.actor
        1 -> actual.minWidth
        2 -> actual.minHeight
        3 -> actual.prefWidth
        4 -> actual.prefHeight
        5 -> actual.maxWidth
        6 -> actual.maxHeight
        7 -> fillX(actual)
        8 -> fillY(actual)
        9 -> actual.align
        10 -> actual.background
        11 -> round(actual)
        12 -> actual.clip
        13 -> ExtentValues(actual.padTopValue, actual.padLeftValue, actual.padBottomValue, actual.padRightValue)
        14 -> fillParent(actual)
        15 -> layoutEnabled(actual)
        16 -> wrapChildren(prop, actual)
        17 -> actual.color
        18 -> actual.name
        19 -> actual.originX
        20 -> actual.originY
        21 -> actual.x
        22 -> actual.y
        23 -> actual.width
        24 -> actual.height
        25 -> actual.rotation
        26 -> actual.scaleX
        27 -> actual.scaleY
        28 -> actual.isVisible
        29 -> actual.debug
        30 -> actual.touchable
        31 -> wrapListeners(prop, actual.listeners)
        32 -> wrapListeners(prop, actual.captureListeners)

        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: Container<Actor>, value: Any?) {
        when (prop) {
            0 -> actual.actor = value as Actor
            1 -> actual.minWidth(value as Value)
            2 -> actual.minHeight(value as Value)
            3 -> actual.prefWidth(value as Value)
            4 -> actual.prefHeight(value as Value)
            5 -> actual.maxWidth(value as Value)
            6 -> actual.maxHeight(value as Value)
            7 -> actual.fill(value as Float, fillY(actual))
            8 -> actual.fill(fillX(actual), value as Float)
            9 -> actual.align(value as Int)
            10 -> actual.background(value as Drawable)
            11 -> actual.setRound(value as Boolean)
            12 -> actual.clip = value as Boolean
            13 -> actual.pad((value as ExtentValues).top, value.left, value.bottom, value.right)
            14 -> actual.setFillParent(value as Boolean)
            15 -> actual.setLayoutEnabled(value as Boolean)
            16 -> updateActualChildren()
            17 -> actual.color = value as Color
            18 -> actual.name = value as String?
            19 -> actual.originX = value as Float
            20 -> actual.originY = value as Float
            21 -> actual.x = value as Float
            22 -> actual.y = value as Float
            23 -> actual.width = value as Float
            24 -> actual.height = value as Float
            25 -> actual.rotation = value as Float
            26 -> actual.scaleX = value as Float
            27 -> actual.scaleY = value as Float
            28 -> actual.isVisible = value as Boolean
            29 -> actual.debug = value as Boolean
            30 -> actual.touchable = value as Touchable
            31 -> throw UnsupportedOperationException()
            32 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
        }
    }
}

inline fun container(
    // VContainer
    minWidth: Value = VContainer.defaultMinWidth,
    minHeight: Value = VContainer.defaultMinHeight,
    prefWidth: Value = VContainer.defaultPrefWidth,
    prefHeight: Value = VContainer.defaultPrefHeight,
    maxWidth: Value = VContainer.defaultMaxWidth,
    maxHeight: Value = VContainer.defaultMaxHeight,
    fillX: Float = VContainer.defaultFillX,
    fillY: Float = VContainer.defaultFillY,
    align: Int = VContainer.defaultAlign,
    background: Drawable? = VContainer.defaultBackground,
    round: Boolean = VContainer.defaultRound,
    clip: Boolean = VContainer.defaultClip,
    pad: ExtentValues = VContainer.defaultPad,

    // VWidgetGroup
    fillParent: Boolean = VWidgetGroup.defaultFillParent,
    layoutEnabled: Boolean = VWidgetGroup.defaultLayoutEnabled,

    // VGroup
    children: List<VActor<*>> = VGroup.defaultChildren,

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
    touchable: Touchable = VContainer.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,

    // VRef
    @Suppress("unchecked_cast")
    ref: Ref<Container<Actor>>? = VRef.defaultRef as Ref<Container<Actor>>?,

    // VGroup
    actor: () -> Unit
) = constructParent<VActor<*>, VContainer>(actor) {
    VContainer(
        it.single(),
        minWidth,
        minHeight,
        prefWidth,
        prefHeight,
        maxWidth,
        maxHeight,
        fillX,
        fillY,
        align,
        background,
        round,
        clip,
        pad,
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
    )
}