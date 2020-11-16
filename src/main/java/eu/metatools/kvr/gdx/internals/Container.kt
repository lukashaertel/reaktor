package eu.metatools.kvr.gdx.internals

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import eu.metatools.kvr.gdx.utils.hidden

private val containerRound = hidden<Container<Actor>, Float>("round")

var Container<Actor>.extRound
    get() = containerRound(this)
    set(value) = containerRound(this, value)

