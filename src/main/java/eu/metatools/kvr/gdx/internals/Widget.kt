package eu.metatools.kvr.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.Widget
import eu.metatools.kvr.gdx.utils.hidden


private val widgetFillParent = hidden<Widget, Boolean>("fillParent")

private val widgetLayoutEnabled = hidden<Widget, Boolean>("layoutEnabled")

var Widget.extFillParent
    get() = widgetFillParent(this)
    set(value) = widgetFillParent(this, value)

var Widget.extLayoutEnabled
    get() = widgetLayoutEnabled(this)
    set(value) = widgetLayoutEnabled(this, value)