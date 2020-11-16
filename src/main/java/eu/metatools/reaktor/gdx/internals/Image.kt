package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import eu.metatools.reaktor.gdx.utils.hidden


private val imageScaling = hidden<Image, Scaling>("scaling")
private val imageAlign = hidden<Image, Int>("align")

var Image.extScaling
    get() = imageScaling(this)
    set(value) = imageScaling(this, value)

var Image.extAlign
    get() = imageAlign(this)
    set(value) = imageAlign(this, value)