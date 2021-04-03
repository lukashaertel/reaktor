package eu.metatools.reaktor.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import eu.metatools.reaktor.ex.consumeKey
import eu.metatools.reaktor.gdx.utils.EventMediator
import eu.metatools.reaktor.gdx.utils.resumeEventMediators
import eu.metatools.reaktor.gdx.utils.suspendEventMediators

open class VStage(
    val debugAll: Boolean = defaultDebugAll,
    val viewport: Viewport = defaultViewport,
    val listeners: List<EventListener> = defaultListeners,
    val captureListeners: List<EventListener> = defaultCaptureListeners,
    ref: (Stage) -> Unit = defaultRef,
    key: Any? = consumeKey(),
    init: Receiver<VActor<*>> = {},
) : VRef<Stage>(ref, key) {
    companion object {
        const val defaultDebugAll: Boolean = false
        val defaultViewport: Viewport = ScalingViewport(Scaling.stretch,
            Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        val defaultListeners: List<EventListener> = listOf()
        val defaultCaptureListeners: List<EventListener> = listOf()

        private const val ownProps = 5
    }

    val children: List<VActor<*>> = mutableListOf()

    init {
        children as MutableList
        init(ReceiveMany { children.add(it) })
    }

    override fun create() = Stage(ScreenViewport(OrthographicCamera()))

    override fun assign(actual: Stage) {
        actual.isDebugAll = debugAll
        actual.viewport = viewport
        for (child in children)
            actual.root.addActor(child.make())
        for (listener in listeners)
            actual.root.listeners.add(EventMediator(listener))
        for (listener in captureListeners)
            actual.root.captureListeners.add(EventMediator(listener))

        super.assign(actual)
    }

    override val props = ownProps

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> debugAll
        1 -> viewport
        2 -> children
        3 -> listeners
        4 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: Stage): Any? = when (prop) {
        0 -> actual.isDebugAll
        1 -> actual.viewport
        2 -> wrapChildren(prop, actual.root)
        3 -> wrapListeners(prop, actual.root.listeners)
        4 -> wrapListeners(prop, actual.root.captureListeners)
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: Stage, value: Any?) {
        when (prop) {
            0 -> actual.isDebugAll = value as Boolean
            1 -> actual.viewport = (value as Viewport).also { it.update(Gdx.graphics.width, Gdx.graphics.height, true) }
            2 -> updateActualChildren()
            3 -> throw UnsupportedOperationException()
            4 -> throw UnsupportedOperationException()
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