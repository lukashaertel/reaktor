package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import eu.metatools.reaktor.gdx.utils.hidden


val widgetGroupFillParent = hidden<WidgetGroup, Boolean>("fillParent")

val widgetGroupLayoutEnabled = hidden<WidgetGroup, Boolean>("layoutEnabled")

var WidgetGroup.extFillParent
    get() = widgetGroupFillParent(this)
    set(value) = widgetGroupFillParent(this, value)

var WidgetGroup.extLayoutEnabled
    get() = widgetGroupLayoutEnabled(this)
    set(value) = widgetGroupLayoutEnabled(this, value)