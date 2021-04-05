package eu.metatools.reaktor.gdx.utils

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.Layout
import com.badlogic.gdx.utils.Align
import eu.metatools.reaktor.gdx.data.Extents
import kotlin.math.roundToLong

fun touchToFront() = object : InputListener() {
    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        event.target.toFront()
        return false
    }
}

private const val edgeMove = 32


fun Actor.keepWithinStage() {
    val stage: Stage = stage ?: return
    val camera = stage.camera
    if (camera is OrthographicCamera) {
        val parentWidth = stage.width
        val parentHeight = stage.height
        if (getX(Align.right) - camera.position.x > parentWidth / 2 / camera.zoom) setPosition(camera.position.x + parentWidth / 2 / camera.zoom,
            getY(Align.right),
            Align.right)
        if (getX(Align.left) - camera.position.x < -parentWidth / 2 / camera.zoom) setPosition(camera.position.x - parentWidth / 2 / camera.zoom,
            getY(Align.left),
            Align.left)
        if (getY(Align.top) - camera.position.y > parentHeight / 2 / camera.zoom) setPosition(getX(Align.top),
            camera.position.y + parentHeight / 2 / camera.zoom,
            Align.top)
        if (getY(Align.bottom) - camera.position.y < -parentHeight / 2 / camera.zoom) setPosition(getX(Align.bottom),
            camera.position.y - parentHeight / 2 / camera.zoom,
            Align.bottom)
    } else if (parent === stage.root) {
        val parentWidth = stage.width
        val parentHeight = stage.height
        if (x < 0) x = 0f
        if (right > parentWidth) x = parentWidth - width
        if (y < 0) y = 0f
        if (top > parentHeight) y = parentHeight - height
    }
}

fun windowBehavior(
    areas: Extents,
    resizeBorder: Float = 8f,
    isMovable: Boolean = true,
    keepWithinStage: Boolean = true,
    isResizable: Boolean = false,
    isModal: Boolean = false,
) = object : InputListener() {
    var edge = 0
    var dragging = false

    var startX = 0f
    var startY = 0f
    var lastX = 0f
    var lastY = 0f
    private fun updateEdge(target: Actor, x: Float, y: Float) {
        var border: Float = resizeBorder / 2f
        val width: Float = target.width
        val height: Float = target.height
        val padTop: Float = areas.top
        val padLeft: Float = areas.left
        val padBottom: Float = areas.bottom
        val padRight: Float = areas.right
        val right = width - padRight
        edge = 0
        if (isResizable && x >= padLeft - border && x <= right + border && y >= padBottom - border) {
            if (x < padLeft + border) edge = edge or Align.left
            if (x > right - border) edge = edge or Align.right
            if (y < padBottom + border) edge = edge or Align.bottom
            if (edge != 0) border += 25f
            if (x < padLeft + border) edge = edge or Align.left
            if (x > right - border) edge = edge or Align.right
            if (y < padBottom + border) edge = edge or Align.bottom
        }
        if (isMovable && edge == 0 && y <= height && y >= height - padTop && x >= padLeft && x <= right)
            edge = edgeMove
    }

    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (button == 0) {
            updateEdge(event.target, x, y)
            dragging = edge != 0
            startX = x
            startY = y
            lastX = x - event.target.width
            lastY = y - event.target.height
        }
        return edge != 0 || isModal
    }

    override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        dragging = false
    }

    override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        if (!dragging) return
        var width: Float = event.target.width
        var height: Float = event.target.height
        var windowX: Float = event.target.x
        var windowY: Float = event.target.y
        val minWidth: Float = (event.target as? Layout)?.minWidth ?: 0f
        val minHeight: Float = (event.target as? Layout)?.minHeight ?: 0f
        val stage: Stage? = event.target.stage
        val clampPosition = keepWithinStage && stage != null && event.target.parent === stage.root
        if (edge and edgeMove != 0) {
            val amountX = x - startX
            val amountY = y - startY
            windowX += amountX
            windowY += amountY
        }
        if (edge and Align.left != 0) {
            var amountX = x - startX
            if (width - amountX < minWidth) amountX = -(minWidth - width)
            if (clampPosition && windowX + amountX < 0) amountX = -windowX
            width -= amountX
            windowX += amountX
        }
        if (edge and Align.bottom != 0) {
            var amountY = y - startY
            if (height - amountY < minHeight) amountY = -(minHeight - height)
            if (clampPosition && windowY + amountY < 0) amountY = -windowY
            height -= amountY
            windowY += amountY
        }
        if (edge and Align.right != 0) {
            var amountX = x - lastX - width
            if (width + amountX < minWidth) amountX = minWidth - width
            if (stage != null && clampPosition && windowX + width + amountX > stage.width)
                amountX = stage.width - windowX - width
            width += amountX
        }
        if (edge and Align.top != 0) {
            var amountY = y - lastY - height
            if (height + amountY < minHeight) amountY = minHeight - height
            if (stage != null && clampPosition && windowY + height + amountY > stage.height)
                amountY = stage.height - windowY - height
            height += amountY
        }
        event.target.setBounds(
            windowX.roundToLong().toFloat(),
            windowY.roundToLong().toFloat(),
            width.roundToLong().toFloat(),
            height.roundToLong().toFloat())
        if (keepWithinStage)
            event.target.keepWithinStage()
    }

    override fun mouseMoved(event: InputEvent, x: Float, y: Float): Boolean {
        updateEdge(event.target, x, y)
        return isModal
    }

    override fun scrolled(event: InputEvent, x: Float, y: Float, amount: Int): Boolean {
        return isModal
    }

    override fun keyDown(event: InputEvent, keycode: Int): Boolean {
        return isModal
    }

    override fun keyUp(event: InputEvent, keycode: Int): Boolean {
        return isModal
    }

    override fun keyTyped(event: InputEvent, character: Char): Boolean {
        return isModal
    }
}