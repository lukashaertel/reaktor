package eu.metatools.kvr.gdx.utils

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.utils.DelayedRemovalArray

/**
 * Settable and suspendable gate for [EventListener]. Actors cannot set their listeners during iteration, which is
 * required for some parts of reconciliation.
 */
class EventMediator(var handler: EventListener, var suspended: Boolean = false) : EventListener {
    override fun handle(event: Event?) =
        if (suspended) false else handler.handle(event)
}

/**
 * Suspends all listeners that are mediators.
 */
fun DelayedRemovalArray<EventListener>.suspendEventMediators() {
    for (listener in this)
        if (listener is EventMediator)
            listener.suspended = true
}

/**
 * Resumes all listeners that are mediators.
 */
fun DelayedRemovalArray<EventListener>.resumeEventMediators() {
    for (listener in this)
        if (listener is EventMediator)
            listener.suspended = false
}