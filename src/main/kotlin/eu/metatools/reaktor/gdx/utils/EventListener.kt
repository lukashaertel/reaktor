package eu.metatools.reaktor.gdx.utils

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent

/**
 * Type tracked event listener.
 */
inline fun <reified T> eventListener(crossinline block: (T) -> Boolean) =
    object : EventListener {
        val name = T::class.simpleName

        override fun handle(it: Event): Boolean {
            return if (it is T) block(it) else false
        }

        override fun toString() = "EventListener $name { ... }"
    }

/**
 * Input event listener.
 */
inline fun inputListener(crossinline block: (InputEvent) -> Boolean) = eventListener(block)

/**
 * Input event listener. Will only run if [type] is assigned, then mark as handled.
 * @param type The input event type to listen for.
 */
inline fun inputListener(type: InputEvent.Type, noPointer: Boolean = false, crossinline block: (InputEvent) -> Unit) =
    inputListener {
        if (it.type == type && (!noPointer || it.pointer < 0)) {
            block(it)
            true
        } else {
            false
        }
    }
