package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

data class VScrollPane(
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

    override val fillParent: Boolean,
    override val layoutEnabled: Boolean,
    override val children: List<VActor<*>>,
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
    override val ref: Ref<ScrollPane>?
) : VWidgetGroup<ScrollPane>() {
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

        val flickScroll = hidden<ScrollPane, Boolean>("flickScroll")
        val overscrollX = hidden<ScrollPane, Boolean>("overscrollX")
        val overscrollY = hidden<ScrollPane, Boolean>("overscrollY")
        val overscrollSpeedMin = hidden<ScrollPane, Float>("overscrollSpeedMin")
        val overscrollSpeedMax = hidden<ScrollPane, Float>("overscrollSpeedMax")
        val flingTime = hidden<ScrollPane, Float>("flingTime")
        val clamp = hidden<ScrollPane, Boolean>("clamp")
        val vScrollOnRight = hidden<ScrollPane, Boolean>("vScrollOnRight")
        val hScrollOnBottom = hidden<ScrollPane, Boolean>("hScrollOnBottom")
        val fadeAlphaSeconds = hidden<ScrollPane, Float>("fadeAlphaSeconds")
        val fadeDelaySeconds = hidden<ScrollPane, Float>("fadeDelaySeconds")
        val scrollBarTouch = hidden<ScrollPane, Boolean>("scrollBarTouch")
        val smoothScrolling = hidden<ScrollPane, Boolean>("smoothScrolling")
        val scrollbarsOnTop = hidden<ScrollPane, Boolean>("scrollbarsOnTop")
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

    override fun props() = 44

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
        25 -> fillParent
        26 -> layoutEnabled
        27 -> children
        28 -> color
        29 -> name
        30 -> originX
        31 -> originY
        32 -> x
        33 -> y
        34 -> width
        35 -> height
        36 -> rotation
        37 -> scaleX
        38 -> scaleY
        39 -> visible
        40 -> debug
        41 -> touchable
        42 -> listeners
        43 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: ScrollPane): Any? = when (prop) {
        0 -> actual.actor
        1 -> actual.style
        2 -> actual.scrollX
        3 -> actual.scrollY
        4 -> flickScroll(actual)
        5 -> actual.isScrollingDisabledX
        6 -> actual.isScrollingDisabledY
        7 -> overscrollX(actual)
        8 -> overscrollY(actual)
        9 -> actual.overscrollDistance
        10 -> overscrollSpeedMin(actual)
        11 -> overscrollSpeedMax(actual)
        12 -> actual.isForceScrollX
        13 -> actual.isForceScrollY
        14 -> flingTime(actual)
        15 -> clamp(actual)
        16 -> vScrollOnRight(actual)
        17 -> hScrollOnBottom(actual)
        18 -> actual.fadeScrollBars
        19 -> fadeAlphaSeconds(actual)
        20 -> fadeDelaySeconds(actual)
        21 -> scrollBarTouch(actual)
        22 -> smoothScrolling(actual)
        23 -> scrollbarsOnTop(actual)
        24 -> actual.variableSizeKnobs
        25 -> fillParent(actual)
        26 -> layoutEnabled(actual)
        27 -> wrapChildren(prop, actual)
        28 -> actual.color
        29 -> actual.name
        30 -> actual.originX
        31 -> actual.originY
        32 -> actual.x
        33 -> actual.y
        34 -> actual.width
        35 -> actual.height
        36 -> actual.rotation
        37 -> actual.scaleX
        38 -> actual.scaleY
        39 -> actual.isVisible
        40 -> actual.debug
        41 -> actual.touchable
        42 -> wrapListeners(prop, actual.listeners)
        43 -> wrapListeners(prop, actual.captureListeners)
        else -> throw IndexOutOfBoundsException(prop)
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
            7 -> actual.setOverscroll(value as Boolean, overscrollY(actual))
            8 -> actual.setOverscroll(overscrollX(actual), value as Boolean)
            9 -> actual.setupOverscroll(value as Float, overscrollSpeedMin(actual), overscrollSpeedMax(actual))
            10 -> actual.setupOverscroll(actual.overscrollDistance, value as Float, overscrollSpeedMax(actual))
            11 -> actual.setupOverscroll(actual.overscrollDistance, overscrollSpeedMin(actual), value as Float)
            12 -> actual.setForceScroll(value as Boolean, actual.isForceScrollY)
            13 -> actual.setForceScroll(actual.isForceScrollX, value as Boolean)
            14 -> actual.setFlingTime(value as Float)
            15 -> actual.setClamp(value as Boolean)
            16 -> actual.setScrollBarPositions(hScrollOnBottom(actual), value as Boolean)
            17 -> actual.setScrollBarPositions(value as Boolean, vScrollOnRight(actual))
            18 -> actual.fadeScrollBars = value as Boolean
            19 -> actual.setupFadeScrollBars(value as Float, fadeDelaySeconds(actual))
            20 -> actual.setupFadeScrollBars(fadeAlphaSeconds(actual), value as Float)
            21 -> actual.setScrollBarTouch(value as Boolean)
            22 -> actual.setSmoothScrolling(value as Boolean)
            23 -> actual.setScrollbarsOnTop(value as Boolean)
            24 -> actual.variableSizeKnobs = value as Boolean
            25 -> actual.setFillParent(value as Boolean)
            26 -> actual.setLayoutEnabled(value as Boolean)
            27 -> updateActualChildren()
            28 -> actual.color = value as Color
            29 -> actual.name = value as String?
            30 -> actual.originX = value as Float
            31 -> actual.originY = value as Float
            32 -> actual.x = value as Float
            33 -> actual.y = value as Float
            34 -> actual.width = value as Float
            35 -> actual.height = value as Float
            36 -> actual.rotation = value as Float
            37 -> actual.scaleX = value as Float
            38 -> actual.scaleY = value as Float
            39 -> actual.isVisible = value as Boolean
            40 -> actual.debug = value as Boolean
            41 -> actual.touchable = value as Touchable
            42 -> throw UnsupportedOperationException()
            43 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
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
    @Suppress("unchecked_cast")
    ref: Ref<ScrollPane>? = VRef.defaultRef as Ref<ScrollPane>?,

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