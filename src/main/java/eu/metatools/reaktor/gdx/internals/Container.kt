package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import eu.metatools.reaktor.gdx.utils.hidden

private val containerRound = hidden<Container<Actor>, Boolean>("round")

var Container<Actor>.extRound
    get() = containerRound(this)
    set(value) = containerRound(this, value)

