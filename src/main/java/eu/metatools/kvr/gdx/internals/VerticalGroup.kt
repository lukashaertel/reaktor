package eu.metatools.kvr.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import eu.metatools.kvr.gdx.utils.hidden

private val verticalGroupRound = hidden<VerticalGroup, Boolean>("round")
private val verticalGroupColumnAlign = hidden<VerticalGroup, Int>("columnAlign")

var VerticalGroup.extRound
    get() = verticalGroupRound(this)
    set(value) = verticalGroupRound(this, value)

var VerticalGroup.extColumnAlign
    get() = verticalGroupColumnAlign(this)
    set(value) = verticalGroupColumnAlign(this, value)