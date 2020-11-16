package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import eu.metatools.kvr.gdx.data.Ref

data class VStack(
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
    override val ref: Ref<Stack>? = null
) : VWidgetGroup<Stack>() {
    companion object {
        val defaultTouchable = Touchable.childrenOnly
    }

    override fun create() = Stack()

    override fun props() = 19

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> fillParent
        1 -> layoutEnabled
        2 -> children
        3 -> color
        4 -> name
        5 -> originX
        6 -> originY
        7 -> x
        8 -> y
        9 -> width
        10 -> height
        11 -> rotation
        12 -> scaleX
        13 -> scaleY
        14 -> visible
        15 -> debug
        16 -> debug
        17 -> listeners
        18 -> captureListeners

        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: Stack): Any? = when (prop) {
        0 -> fillParent(actual)
        1 -> layoutEnabled(actual)
        2 -> wrapChildren(prop, actual)
        3 -> actual.color
        4 -> actual.name
        5 -> actual.originX
        6 -> actual.originY
        7 -> actual.x
        8 -> actual.y
        9 -> actual.width
        10 -> actual.height
        11 -> actual.rotation
        12 -> actual.scaleX
        13 -> actual.scaleY
        14 -> actual.isVisible
        15 -> actual.debug
        16 -> actual.touchable
        17 -> wrapListeners(prop, actual.listeners)
        18 -> wrapListeners(prop, actual.captureListeners)
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: Stack, value: Any?) {
        when (prop) {
            0 -> actual.setFillParent(value as Boolean)
            1 -> actual.setLayoutEnabled(value as Boolean)
            2 -> updateActualChildren()
            3 -> actual.color = value as Color
            4 -> actual.name = value as String?
            5 -> actual.originX = value as Float
            6 -> actual.originY = value as Float
            7 -> actual.x = value as Float
            8 -> actual.y = value as Float
            9 -> actual.width = value as Float
            10 -> actual.height = value as Float
            11 -> actual.rotation = value as Float
            12 -> actual.scaleX = value as Float
            13 -> actual.scaleY = value as Float
            14 -> actual.isVisible = value as Boolean
            15 -> actual.debug = value as Boolean
            16 -> actual.touchable = value as Touchable
            17 -> throw UnsupportedOperationException()
            18 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
        }
    }
}

inline fun stack(
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
    touchable: Touchable = VStack.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,

    // VRef
    @Suppress("unchecked_cast")
    ref: Ref<Stack>? = VRef.defaultRef as Ref<Stack>?,

    // VGroup
    children: () -> Unit
) = constructParent<VActor<*>, VStack>(children) {
    VStack(
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