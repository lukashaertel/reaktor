package eu.metatools.reaktor.gdx.utils

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Value

@Suppress("classname")
object ofOwnHeight : ActorAsNameOnly()

@Suppress("classname")
object ofOwnWidth : ActorAsNameOnly()

@Suppress("classname")
class ofWidth(val of: Actor) : ActorAsNameOnly()

@Suppress("classname")
class ofHeight(val of: Actor) : ActorAsNameOnly()

val Number.px get() = Value.Fixed(this.toFloat())

@Suppress("unused_parameter")
operator fun Number.rem(actor: ofOwnHeight): Value =
    Value.percentHeight(this.toFloat())

@Suppress("unused_parameter")
operator fun Number.rem(actor: ofOwnWidth): Value =
    Value.percentHeight(this.toFloat())

@Suppress("unused_parameter")
operator fun Number.rem(actor: ofWidth): Value =
    Value.percentHeight(this.toFloat(), actor.of)

@Suppress("unused_parameter")
operator fun Number.rem(actor: ofHeight): Value =
    Value.percentHeight(this.toFloat(), actor.of)
