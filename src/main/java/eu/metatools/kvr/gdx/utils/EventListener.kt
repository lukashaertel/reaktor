package eu.metatools.kvr.gdx.utils

import com.badlogic.gdx.scenes.scene2d.EventListener

/**
 * Type tracked event listener.
 */
inline fun <reified T> eventListener(crossinline block: (T) -> Boolean) =
    EventListener {
        if (it is T) block(it) else false
    }
