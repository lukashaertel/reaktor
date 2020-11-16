package eu.metatools.reaktor.gdx.data

data class Rect(val x: Float, val y: Float, val width: Float, val height: Float) {
    companion object {
        val empty = Rect(0f, 0f, 0f, 0f)
    }
}