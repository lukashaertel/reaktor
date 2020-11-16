package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.EventMediator
import eu.metatools.kvr.gdx.utils.resumeEventMediators
import eu.metatools.kvr.gdx.utils.suspendEventMediators

// TODO: Viewport
data class VStage(
    // VStage
    val debugAll: Boolean,
    val children: List<VActor<*>>,
    val listeners: List<EventListener>,
    val captureListeners: List<EventListener>,

    // VRef
    override val ref: Ref<Stage>?
) : VRef<Stage>() {
    companion object {
        const val defaultDebugAll: Boolean = false
        val defaultListeners: List<EventListener> = listOf()
        val defaultCaptureListeners: List<EventListener> = listOf()
    }

    override fun create() = Stage(ScreenViewport(OrthographicCamera()))

    override fun assign(actual: Stage) {
        actual.isDebugAll = debugAll
        for (child in children)
            actual.root.addActor(child.make())
        for (listener in listeners)
            actual.root.listeners.add(EventMediator(listener))
        for (listener in captureListeners)
            actual.root.captureListeners.add(EventMediator(listener))

        super.assign(actual)
    }

    override fun props() = 4

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> debugAll
        1 -> children
        2 -> listeners
        3 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: Stage): Any? = when (prop) {
        0 -> actual.isDebugAll
        1 -> wrapChildren(prop, actual.root)
        2 -> wrapListeners(prop, actual.root.listeners)
        3 -> wrapListeners(prop, actual.root.captureListeners)
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: Stage, value: Any?) {
        when (prop) {
            0 -> actual.isDebugAll = value as Boolean
            1 -> updateActualChildren()
            2 -> throw UnsupportedOperationException()
            3 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
        }
    }

    override fun begin(actual: Stage) {
        super.begin(actual)
        actual.root.listeners.suspendEventMediators()
    }

    override fun end(actual: Stage) {
        actual.root.listeners.resumeEventMediators()
        super.end(actual)
    }
}

fun stage(
    // VStage
    debugAll: Boolean = VStage.defaultDebugAll,
    listeners: List<EventListener> = VStage.defaultListeners,
    captureListeners: List<EventListener> = VStage.defaultCaptureListeners,

    // VRef
    @Suppress("unchecked_cast")
    ref: Ref<Stage>? = VRef.defaultRef as Ref<Stage>?,
    children: () -> Unit
) = constructParent<VActor<*>, VStage>(children) {
    VStage(
        debugAll,
        it,
        listeners,
        captureListeners,
        ref
    )
}