package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.Window
import eu.metatools.reaktor.gdx.utils.hidden

private val windowResizeBorder = hidden<Window, Int>("resizeBorder")
private val windowKeepInStage = hidden<Window, Boolean>("keepWithinStage")

var Window.extResizeBorder
    get() = windowResizeBorder(this)
    set(value) = windowResizeBorder(this, value)

var Window.extKeepInStage
    get() = windowKeepInStage(this)
    set(value) = windowKeepInStage(this, value)
