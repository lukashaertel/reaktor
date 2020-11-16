package eu.metatools.kvr.gdx

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import eu.metatools.kvr.gdx.utils.hidden

abstract class VWidgetGroup<A : WidgetGroup> : VGroup<A>() {
    companion object {
        const val defaultFillParent = false
        const val defaultLayoutEnabled = true

        val fillParent = hidden<WidgetGroup, Boolean>("fillParent")

        val layoutEnabled = hidden<WidgetGroup, Boolean>("layoutEnabled")
    }

    abstract val fillParent: Boolean

    abstract val layoutEnabled: Boolean

    override fun assign(actual: A) {
        actual.setFillParent(fillParent)
        actual.setLayoutEnabled(layoutEnabled)

        super.assign(actual)
    }
}