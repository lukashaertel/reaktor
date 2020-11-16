package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import eu.metatools.reaktor.gdx.utils.hidden

private val scrollPaneFlickScroll = hidden<ScrollPane, Boolean>("flickScroll")
private val scrollPaneOverscrollX = hidden<ScrollPane, Boolean>("overscrollX")
private val scrollPaneOverscrollY = hidden<ScrollPane, Boolean>("overscrollY")
private val scrollPaneOverscrollSpeedMin = hidden<ScrollPane, Float>("overscrollSpeedMin")
private val scrollPaneOverscrollSpeedMax = hidden<ScrollPane, Float>("overscrollSpeedMax")
private val scrollPaneFlingTime = hidden<ScrollPane, Float>("flingTime")
private val scrollPaneClamp = hidden<ScrollPane, Boolean>("clamp")
private val scrollPaneVScrollOnRight = hidden<ScrollPane, Boolean>("vScrollOnRight")
private val scrollPaneHScrollOnBottom = hidden<ScrollPane, Boolean>("hScrollOnBottom")
private val scrollPaneFadeAlphaSeconds = hidden<ScrollPane, Float>("fadeAlphaSeconds")
private val scrollPaneFadeDelaySeconds = hidden<ScrollPane, Float>("fadeDelaySeconds")
private val scrollPaneScrollBarTouch = hidden<ScrollPane, Boolean>("scrollBarTouch")
private val scrollPaneSmoothScrolling = hidden<ScrollPane, Boolean>("smoothScrolling")
private val scrollPaneScrollbarsOnTop = hidden<ScrollPane, Boolean>("scrollbarsOnTop")

var ScrollPane.extFlickScroll
    get() = scrollPaneFlickScroll(this)
    set(value) = scrollPaneFlickScroll(this, value)

var ScrollPane.extOverscrollX
    get() = scrollPaneOverscrollX(this)
    set(value) = scrollPaneOverscrollX(this, value)

var ScrollPane.extOverscrollY
    get() = scrollPaneOverscrollY(this)
    set(value) = scrollPaneOverscrollY(this, value)

var ScrollPane.extOverscrollSpeedMin
    get() = scrollPaneOverscrollSpeedMin(this)
    set(value) = scrollPaneOverscrollSpeedMin(this, value)

var ScrollPane.extOverscrollSpeedMax
    get() = scrollPaneOverscrollSpeedMax(this)
    set(value) = scrollPaneOverscrollSpeedMax(this, value)

var ScrollPane.extFlingTime
    get() = scrollPaneFlingTime(this)
    set(value) = scrollPaneFlingTime(this, value)

var ScrollPane.extClamp
    get() = scrollPaneClamp(this)
    set(value) = scrollPaneClamp(this, value)

var ScrollPane.extVScrollOnRight
    get() = scrollPaneVScrollOnRight(this)
    set(value) = scrollPaneVScrollOnRight(this, value)

var ScrollPane.extHScrollOnBottom
    get() = scrollPaneHScrollOnBottom(this)
    set(value) = scrollPaneHScrollOnBottom(this, value)

var ScrollPane.extFadeAlphaSeconds
    get() = scrollPaneFadeAlphaSeconds(this)
    set(value) = scrollPaneFadeAlphaSeconds(this, value)

var ScrollPane.extFadeDelaySeconds
    get() = scrollPaneFadeDelaySeconds(this)
    set(value) = scrollPaneFadeDelaySeconds(this, value)

var ScrollPane.extScrollBarTouch
    get() = scrollPaneScrollBarTouch(this)
    set(value) = scrollPaneScrollBarTouch(this, value)

var ScrollPane.extSmoothScrolling
    get() = scrollPaneSmoothScrolling(this)
    set(value) = scrollPaneSmoothScrolling(this, value)

var ScrollPane.extScrollbarsOnTop
    get() = scrollPaneScrollbarsOnTop(this)
    set(value) = scrollPaneScrollbarsOnTop(this, value)