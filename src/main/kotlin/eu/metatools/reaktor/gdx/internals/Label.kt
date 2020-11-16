package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.Label
import eu.metatools.reaktor.gdx.utils.hidden


private val labelWrap = hidden<Label, Boolean>("wrap")

var Label.extWrap
    get() = labelWrap(this)
    set(value) = labelWrap(this, value)