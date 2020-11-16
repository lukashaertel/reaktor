package eu.metatools.kvr.gdx

import com.badlogic.gdx.scenes.scene2d.ui.Widget
import eu.metatools.kvr.gdx.utils.hidden

abstract class VWidget<A : Widget> : VActor<A>() {
    companion object {
        const val defaultFillParent = false
        const val defaultLayoutEnabled = true

        val fillParent = hidden<Widget, Boolean>("fillParent")

        val layoutEnabled = hidden<Widget, Boolean>("layoutEnabled")
    }

    abstract val fillParent: Boolean

    abstract val layoutEnabled: Boolean

    override fun assign(actual: A) {
        actual.setFillParent(fillParent)
        actual.setLayoutEnabled(layoutEnabled)
        super.assign(actual)
    }
}