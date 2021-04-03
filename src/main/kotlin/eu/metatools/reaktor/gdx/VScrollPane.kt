package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import eu.metatools.reaktor.ex.consumeKey
import eu.metatools.reaktor.gdx.internals.*

open class VScrollPane(
    val style: ScrollPaneStyle,
    val scrollX: Float = defaultScrollX,
    val scrollY: Float = defaultScrollY,
    val flickScroll: Boolean = defaultFlickScroll,
    val disableX: Boolean = defaultDisableX,
    val disableY: Boolean = defaultDisableY,
    val overscrollX: Boolean = defaultOverscrollX,
    val overscrollY: Boolean = defaultOverscrollY,
    val overscrollDistance: Float = defaultOverscrollDistance,
    val overscrollSpeedMin: Float = defaultOverscrollSpeedMin,
    val overscrollSpeedMax: Float = defaultOverscrollSpeedMax,
    val forceScrollX: Boolean = defaultForceScrollX,
    val forceScrollY: Boolean = defaultForceScrollY,
    val flingTime: Float = defaultFlingTime,
    val clamp: Boolean = defaultClamp,
    val vScrollOnRight: Boolean = defaultVScrollOnRight,
    val hScrollOnBottom: Boolean = defaultHScrollOnBottom,
    val fadeScrollBars: Boolean = defaultFadeScrollBars,
    val fadeAlphaSeconds: Float = defaultFadeAlphaSeconds,
    val fadeDelaySeconds: Float = defaultFadeDelaySeconds,
    val scrollBarTouch: Boolean = defaultScrollBarTouch,
    val smoothScrolling: Boolean = defaultSmoothScrolling,
    val scrollbarsOnTop: Boolean = defaultScrollbarsOnTop,
    val variableSizeKnobs: Boolean = defaultVariableSizeKnobs,
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
    ref: (ScrollPane) -> Unit = defaultRef,
    key: Any? = consumeKey(),
    init: ReceiverActorChildren = {},
) : VWidgetGroup<ScrollPane>(
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
    init.toChildren()
) {
    companion object {
        const val defaultScrollX: Float = 0f
        const val defaultScrollY: Float = 0f
        const val defaultFlickScroll: Boolean = true
        const val defaultDisableX: Boolean = false
        const val defaultDisableY: Boolean = false
        const val defaultOverscrollX: Boolean = true
        const val defaultOverscrollY: Boolean = true
        const val defaultOverscrollDistance: Float = 50f
        const val defaultOverscrollSpeedMin: Float = 30f
        const val defaultOverscrollSpeedMax: Float = 200f
        const val defaultForceScrollX: Boolean = false
        const val defaultForceScrollY: Boolean = false
        const val defaultFlingTime: Float = 1f
        const val defaultClamp: Boolean = true
        const val defaultVScrollOnRight: Boolean = true
        const val defaultHScrollOnBottom: Boolean = true
        const val defaultFadeScrollBars: Boolean = true
        const val defaultFadeAlphaSeconds: Float = 1f
        const val defaultFadeDelaySeconds: Float = 1f
        const val defaultScrollBarTouch: Boolean = true
        const val defaultSmoothScrolling: Boolean = true
        const val defaultScrollbarsOnTop: Boolean = false
        const val defaultVariableSizeKnobs: Boolean = true
        private const val ownProps = 25
    }

    var actor: VActor<*>? = VCell.defaultActor
        private set

    init {
        init.toActor()(ReceiveOne { actor = it })
    }

    override fun create() = ScrollPane(actor?.make(), style)

    override fun assign(actual: ScrollPane) {
        actual.scrollX = scrollX
        actual.scrollY = scrollY
        actual.setFlickScroll(flickScroll)
        actual.setScrollingDisabled(disableX, disableY)
        actual.setOverscroll(overscrollX, overscrollY)
        actual.setupOverscroll(overscrollDistance, overscrollSpeedMin, overscrollSpeedMax)
        actual.setForceScroll(forceScrollX, forceScrollY)
        actual.setFlingTime(flingTime)
        actual.setClamp(clamp)
        actual.setScrollBarPositions(hScrollOnBottom, vScrollOnRight)
        actual.fadeScrollBars = fadeScrollBars
        actual.setupFadeScrollBars(fadeAlphaSeconds, fadeDelaySeconds)
        actual.setScrollBarTouch(scrollBarTouch)
        actual.setSmoothScrolling(smoothScrolling)
        actual.setScrollbarsOnTop(scrollbarsOnTop)
        actual.variableSizeKnobs = variableSizeKnobs
        super.assign(actual)
    }

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> actor
        1 -> style
        2 -> scrollX
        3 -> scrollY
        4 -> flickScroll
        5 -> disableX
        6 -> disableY
        7 -> overscrollX
        8 -> overscrollY
        9 -> overscrollDistance
        10 -> overscrollSpeedMin
        11 -> overscrollSpeedMax
        12 -> forceScrollX
        13 -> forceScrollY
        14 -> flingTime
        15 -> clamp
        16 -> vScrollOnRight
        17 -> hScrollOnBottom
        18 -> fadeScrollBars
        19 -> fadeAlphaSeconds
        20 -> fadeDelaySeconds
        21 -> scrollBarTouch
        22 -> smoothScrolling
        23 -> scrollbarsOnTop
        24 -> variableSizeKnobs
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: ScrollPane): Any? = when (prop) {
        0 -> actual.actor
        1 -> actual.style
        2 -> actual.scrollX
        3 -> actual.scrollY
        4 -> actual.extFlickScroll
        5 -> actual.isScrollingDisabledX
        6 -> actual.isScrollingDisabledY
        7 -> actual.extOverscrollX
        8 -> actual.extOverscrollY
        9 -> actual.overscrollDistance
        10 -> actual.extOverscrollSpeedMin
        11 -> actual.extOverscrollSpeedMax
        12 -> actual.isForceScrollX
        13 -> actual.isForceScrollY
        14 -> actual.extFlingTime
        15 -> actual.extClamp
        16 -> actual.extVScrollOnRight
        17 -> actual.extHScrollOnBottom
        18 -> actual.fadeScrollBars
        19 -> actual.extFadeAlphaSeconds
        20 -> actual.extFadeDelaySeconds
        21 -> actual.extScrollBarTouch
        22 -> actual.extSmoothScrolling
        23 -> actual.extScrollbarsOnTop
        24 -> actual.variableSizeKnobs
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: ScrollPane, value: Any?) {
        when (prop) {
            0 -> actual.actor = value as Actor?
            1 -> actual.style = value as ScrollPaneStyle?
            2 -> actual.scrollX = value as Float
            3 -> actual.scrollY = value as Float
            4 -> actual.setFlickScroll(value as Boolean)
            5 -> actual.setScrollingDisabled(value as Boolean, actual.isScrollingDisabledY)
            6 -> actual.setScrollingDisabled(actual.isScrollingDisabledX, value as Boolean)
            7 -> actual.setOverscroll(value as Boolean, actual.extOverscrollY)
            8 -> actual.setOverscroll(actual.extOverscrollY, value as Boolean)
            9 -> actual.setupOverscroll(value as Float, actual.extOverscrollSpeedMin, actual.extOverscrollSpeedMax)
            10 -> actual.setupOverscroll(actual.overscrollDistance, value as Float, actual.extOverscrollSpeedMax)
            11 -> actual.setupOverscroll(actual.overscrollDistance, actual.extOverscrollSpeedMin, value as Float)
            12 -> actual.setForceScroll(value as Boolean, actual.isForceScrollY)
            13 -> actual.setForceScroll(actual.isForceScrollX, value as Boolean)
            14 -> actual.setFlingTime(value as Float)
            15 -> actual.setClamp(value as Boolean)
            16 -> actual.setScrollBarPositions(actual.extHScrollOnBottom, value as Boolean)
            17 -> actual.setScrollBarPositions(value as Boolean, actual.extVScrollOnRight)
            18 -> actual.fadeScrollBars = value as Boolean
            19 -> actual.setupFadeScrollBars(value as Float, actual.extFadeDelaySeconds)
            20 -> actual.setupFadeScrollBars(actual.extFadeAlphaSeconds, value as Float)
            21 -> actual.setScrollBarTouch(value as Boolean)
            22 -> actual.setSmoothScrolling(value as Boolean)
            23 -> actual.setScrollbarsOnTop(value as Boolean)
            24 -> actual.variableSizeKnobs = value as Boolean
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }
}