package eu.metatools.reaktor.gdx.utils

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable


fun TextureRegion?.asDrawable() =
    if (this == null) BaseDrawable() else TextureRegionDrawable(this)