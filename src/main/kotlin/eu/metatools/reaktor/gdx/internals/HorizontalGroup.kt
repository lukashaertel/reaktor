package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import eu.metatools.reaktor.gdx.utils.hidden

val horizontalGroupRound = hidden<HorizontalGroup, Boolean>("round")
val horizontalGroupRowAlign = hidden<HorizontalGroup, Int>("rowAlign")

var HorizontalGroup.extRound
    get() = horizontalGroupRound(this)
    set(value) = horizontalGroupRound(this, value)

var HorizontalGroup.extRowAlign
    get() = horizontalGroupRowAlign(this)
    set(value) = horizontalGroupRowAlign(this, value)