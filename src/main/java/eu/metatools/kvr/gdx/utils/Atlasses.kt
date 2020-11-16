package eu.metatools.kvr.gdx.utils

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import java.util.*

private val gotten = WeakHashMap<TextureAtlas, HashMap<Any, TextureAtlas.AtlasRegion?>>()

operator fun TextureAtlas.get(name: String): TextureAtlas.AtlasRegion? =
    gotten.getOrPut(this) { HashMap() }.getOrPut(name) { findRegion(name) }

operator fun TextureAtlas.get(name: String, index: Int): TextureAtlas.AtlasRegion? =
    gotten.getOrPut(this) { HashMap() }.getOrPut(name to index) { findRegion(name) }