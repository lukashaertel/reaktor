package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import eu.metatools.reaktor.gdx.data.Ref
import eu.metatools.reaktor.gdx.internals.*

open class VScrollPane(
    val actor: VActor<*>,
    val style: ScrollPaneStyle,
    val scrollX: Float,
    val scrollY: Float,
    val flickScroll: Boolean,
    val disableX: Boolean,
    val disableY: Boolean,
    val overscrollX: Boolean,
    val overscrollY: Boolean,
    val overscrollDistance: Float,
    val overscrollSpeedMin: Float,
    val overscrollSpeedMax: Float,
    val forceScrollX: Boolean,
    val forceScrollY: Boolean,
    val flingTime: Float,
    val clamp: Boolean,
    val vScrollOnRight: Boolean,
    val hScrollOnBottom: Boolean,
    val fadeScrollBars: Boolean,
    val fadeAlphaSeconds: Float,
    val fadeDelaySeconds: Float,
    val scrollBarTouch: Boolean,
    val smoothScrolling: Boolean,
    val scrollbarsOnTop: Boolean,
    val variableSizeKnobs: Boolean,
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
    ref: Ref?
) : VWidgetGroup<ScrollPane>(
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

    override fun create() = ScrollPane(actor.make(), style)

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

inline fun scrollPane(
    style: ScrollPaneStyle,
    scrollX: Float = VScrollPane.defaultScrollX,
    scrollY: Float = VScrollPane.defaultScrollY,
    flickScroll: Boolean = VScrollPane.defaultFlickScroll,
    disableX: Boolean = VScrollPane.defaultDisableX,
    disableY: Boolean = VScrollPane.defaultDisableY,
    overscrollX: Boolean = VScrollPane.defaultOverscrollX,
    overscrollY: Boolean = VScrollPane.defaultOverscrollY,
    overscrollDistance: Float = VScrollPane.defaultOverscrollDistance,
    overscrollSpeedMin: Float = VScrollPane.defaultOverscrollSpeedMin,
    overscrollSpeedMax: Float = VScrollPane.defaultOverscrollSpeedMax,
    forceScrollX: Boolean = VScrollPane.defaultForceScrollX,
    forceScrollY: Boolean = VScrollPane.defaultForceScrollY,
    flingTime: Float = VScrollPane.defaultFlingTime,
    clamp: Boolean = VScrollPane.defaultClamp,
    vScrollOnRight: Boolean = VScrollPane.defaultVScrollOnRight,
    hScrollOnBottom: Boolean = VScrollPane.defaultHScrollOnBottom,
    fadeScrollBars: Boolean = VScrollPane.defaultFadeScrollBars,
    fadeAlphaSeconds: Float = VScrollPane.defaultFadeAlphaSeconds,
    fadeDelaySeconds: Float = VScrollPane.defaultFadeDelaySeconds,
    scrollBarTouch: Boolean = VScrollPane.defaultScrollBarTouch,
    smoothScrolling: Boolean = VScrollPane.defaultSmoothScrolling,
    scrollbarsOnTop: Boolean = VScrollPane.defaultScrollbarsOnTop,
    variableSizeKnobs: Boolean = VScrollPane.defaultVariableSizeKnobs,

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
    touchable: Touchable = VActor.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,

    // VRef
    ref: Ref? = VRef.defaultRef,

    // VScrollPane
    actor: () -> Unit
) = constructParent<VActor<*>, VScrollPane>(actor) {
    VScrollPane(
        it.single(),
        style,
        scrollX,
        scrollY,
        flickScroll,
        disableX,
        disableY,
        overscrollX,
        overscrollY,
        overscrollDistance,
        overscrollSpeedMin,
        overscrollSpeedMax,
        forceScrollX,
        forceScrollY,
        flingTime,
        clamp,
        vScrollOnRight,
        hScrollOnBottom,
        fadeScrollBars,
        fadeAlphaSeconds,
        fadeDelaySeconds,
        scrollBarTouch,
        smoothScrolling,
        scrollbarsOnTop,
        variableSizeKnobs,
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