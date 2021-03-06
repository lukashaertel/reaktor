package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import eu.metatools.reaktor.ex.consumeKey
import eu.metatools.reaktor.gdx.data.ExtentValues
import eu.metatools.reaktor.gdx.internals.extRound
import eu.metatools.reaktor.gdx.utils.generateAtMostOne
import eu.metatools.reaktor.gdx.utils.generateMany
import eu.metatools.reaktor.gdx.utils.tryReceive

open class VContainer (
    val minWidth: Value = defaultMinWidth,
    val minHeight: Value = defaultMinHeight,
    val prefWidth: Value = defaultPrefWidth,
    val prefHeight: Value = defaultPrefHeight,
    val maxWidth: Value = defaultMaxWidth,
    val maxHeight: Value = defaultMaxHeight,
    val fillX: Float = defaultFillX,
    val fillY: Float = defaultFillY,
    val align: Int = defaultAlign,
    val background: Drawable? = defaultBackground,
    val round: Boolean = defaultRound,
    val clip: Boolean = defaultClip,
    val pad: ExtentValues = defaultPad,
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
    ref: (Container<Actor>) -> Unit = defaultRef,
    key: Any? = consumeKey(),
    children: List<VActor<*>> = defaultChildren,
    val actor: VActor<*>? = defaultActor,
) : VWidgetGroup<Container<Actor>>(
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
    ref,
    key,
    children
) {
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
        val defaultActor: VActor<*>? = null

        private const val ownProps = 14
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
        actual.actor = actor?.make()

        super.assign(actual)
    }

    override val props = ownProps + super.props

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
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: Container<Actor>): Any? = when (prop) {
        0 -> actual.actor
        1 -> actual.minWidth
        2 -> actual.minHeight
        3 -> actual.prefWidth
        4 -> actual.prefHeight
        5 -> actual.maxWidth
        6 -> actual.maxHeight
        7 -> actual.fillX
        8 -> actual.fillY
        9 -> actual.align
        10 -> actual.background
        11 -> actual.extRound
        12 -> actual.clip
        13 -> ExtentValues(actual.padTopValue, actual.padLeftValue, actual.padBottomValue, actual.padRightValue)
        else -> super.getActual(prop - ownProps, actual)
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
            7 -> actual.fill(value as Float, actual.fillY)
            8 -> actual.fill(actual.fillX, value as Float)
            9 -> actual.align(value as Int)
            10 -> actual.background(value as Drawable)
            11 -> actual.setRound(value as Boolean)
            12 -> actual.clip = value as Boolean
            13 -> actual.pad((value as ExtentValues).top, value.left, value.bottom, value.right)
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }
}

inline fun container(
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
    fillParent: Boolean = VWidgetGroup.defaultFillParent,
    layoutEnabled: Boolean = VWidgetGroup.defaultLayoutEnabled,
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
    noinline ref: (Container<Actor>) -> Unit = VRef.defaultRef,
    key: Any? = consumeKey(),
    generateChildren: ()->Unit = {},
    generateActor: ()->Unit = {},
): VContainer {
    val children = generateMany<VActor<*>>(generateChildren)
    val actor = generateAtMostOne<VActor<*>>(generateActor)
    return VContainer(
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
        ref,
        key,
        children,
        actor
    ).tryReceive()
}