package eu.metatools.kvr.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.Label
import eu.metatools.kvr.gdx.utils.hidden


private val labelWrap = hidden<Label, Boolean>("wrap")

var Label.extWrap
    get() = labelWrap(this)
    set(value) = labelWrap(this, value)