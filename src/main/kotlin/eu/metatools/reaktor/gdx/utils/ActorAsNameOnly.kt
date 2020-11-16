package eu.metatools.reaktor.gdx.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.*

/**
 * Base type for static class names.
 */
abstract class ActorAsNameOnly : Actor() {
    override fun draw(batch: Batch?, parentAlpha: Float) = throw UnsupportedOperationException()

    override fun act(delta: Float) = throw UnsupportedOperationException()

    override fun fire(event: Event?) = throw UnsupportedOperationException()

    override fun notify(event: Event?, capture: Boolean) = throw UnsupportedOperationException()

    override fun hit(x: Float, y: Float, touchable: Boolean) = throw UnsupportedOperationException()

    override fun remove() = throw UnsupportedOperationException()

    override fun addListener(listener: EventListener?) = throw UnsupportedOperationException()

    override fun removeListener(listener: EventListener?) = throw UnsupportedOperationException()

    override fun getListeners() = throw UnsupportedOperationException()

    override fun addCaptureListener(listener: EventListener?) = throw UnsupportedOperationException()

    override fun removeCaptureListener(listener: EventListener?) = throw UnsupportedOperationException()

    override fun getCaptureListeners() = throw UnsupportedOperationException()

    override fun addAction(action: Action?) = throw UnsupportedOperationException()

    override fun removeAction(action: Action?) = throw UnsupportedOperationException()

    override fun getActions() = throw UnsupportedOperationException()

    override fun hasActions() = throw UnsupportedOperationException()

    override fun clearActions() = throw UnsupportedOperationException()

    override fun clearListeners() = throw UnsupportedOperationException()

    override fun clear() = throw UnsupportedOperationException()

    override fun getStage() = throw UnsupportedOperationException()

    override fun setStage(stage: Stage?) = throw UnsupportedOperationException()

    override fun isDescendantOf(actor: Actor?) = throw UnsupportedOperationException()

    override fun isAscendantOf(actor: Actor?) = throw UnsupportedOperationException()

    override fun <T : Actor?> firstAscendant(type: Class<T>?) = throw UnsupportedOperationException()

    override fun hasParent() = throw UnsupportedOperationException()

    override fun getParent() = throw UnsupportedOperationException()

    override fun setParent(parent: Group?) = throw UnsupportedOperationException()

    override fun isTouchable() = throw UnsupportedOperationException()

    override fun getTouchable() = throw UnsupportedOperationException()

    override fun setTouchable(touchable: Touchable?) = throw UnsupportedOperationException()

    override fun isVisible() = throw UnsupportedOperationException()

    override fun setVisible(visible: Boolean) = throw UnsupportedOperationException()

    override fun ancestorsVisible() = throw UnsupportedOperationException()

    override fun hasKeyboardFocus() = throw UnsupportedOperationException()

    override fun hasScrollFocus() = throw UnsupportedOperationException()

    override fun isTouchFocusTarget() = throw UnsupportedOperationException()

    override fun isTouchFocusListener() = throw UnsupportedOperationException()

    override fun getUserObject() = throw UnsupportedOperationException()

    override fun setUserObject(userObject: Any?) = throw UnsupportedOperationException()

    override fun getX() = throw UnsupportedOperationException()

    override fun getX(alignment: Int) = throw UnsupportedOperationException()

    override fun setX(x: Float) = throw UnsupportedOperationException()

    override fun setX(x: Float, alignment: Int) = throw UnsupportedOperationException()

    override fun getY() = throw UnsupportedOperationException()

    override fun getY(alignment: Int) = throw UnsupportedOperationException()

    override fun setY(y: Float) = throw UnsupportedOperationException()

    override fun setY(y: Float, alignment: Int) = throw UnsupportedOperationException()

    override fun setPosition(x: Float, y: Float) = throw UnsupportedOperationException()

    override fun setPosition(x: Float, y: Float, alignment: Int) = throw UnsupportedOperationException()

    override fun moveBy(x: Float, y: Float) = throw UnsupportedOperationException()

    override fun getWidth() = throw UnsupportedOperationException()

    override fun setWidth(width: Float) = throw UnsupportedOperationException()

    override fun getHeight() = throw UnsupportedOperationException()

    override fun setHeight(height: Float) = throw UnsupportedOperationException()

    override fun getTop() = throw UnsupportedOperationException()

    override fun getRight() = throw UnsupportedOperationException()

    override fun positionChanged() = throw UnsupportedOperationException()

    override fun sizeChanged() = throw UnsupportedOperationException()

    override fun rotationChanged() = throw UnsupportedOperationException()

    override fun setSize(width: Float, height: Float) = throw UnsupportedOperationException()

    override fun sizeBy(size: Float) = throw UnsupportedOperationException()

    override fun sizeBy(width: Float, height: Float) = throw UnsupportedOperationException()

    override fun setBounds(x: Float, y: Float, width: Float, height: Float) = throw UnsupportedOperationException()

    override fun getOriginX() = throw UnsupportedOperationException()

    override fun setOriginX(originX: Float) = throw UnsupportedOperationException()

    override fun getOriginY() = throw UnsupportedOperationException()

    override fun setOriginY(originY: Float) = throw UnsupportedOperationException()

    override fun setOrigin(originX: Float, originY: Float) = throw UnsupportedOperationException()

    override fun setOrigin(alignment: Int) = throw UnsupportedOperationException()

    override fun getScaleX() = throw UnsupportedOperationException()

    override fun setScaleX(scaleX: Float) = throw UnsupportedOperationException()

    override fun getScaleY() = throw UnsupportedOperationException()

    override fun setScaleY(scaleY: Float) = throw UnsupportedOperationException()

    override fun setScale(scaleXY: Float) = throw UnsupportedOperationException()

    override fun setScale(scaleX: Float, scaleY: Float) = throw UnsupportedOperationException()

    override fun scaleBy(scale: Float) = throw UnsupportedOperationException()

    override fun scaleBy(scaleX: Float, scaleY: Float) = throw UnsupportedOperationException()

    override fun getRotation() = throw UnsupportedOperationException()

    override fun setRotation(degrees: Float) = throw UnsupportedOperationException()

    override fun rotateBy(amountInDegrees: Float) = throw UnsupportedOperationException()

    override fun setColor(color: Color?) = throw UnsupportedOperationException()

    override fun setColor(r: Float, g: Float, b: Float, a: Float) = throw UnsupportedOperationException()

    override fun getColor() = throw UnsupportedOperationException()

    override fun getName() = throw UnsupportedOperationException()

    override fun setName(name: String?) = throw UnsupportedOperationException()

    override fun toFront() = throw UnsupportedOperationException()

    override fun toBack() = throw UnsupportedOperationException()

    override fun setZIndex(index: Int) = throw UnsupportedOperationException()

    override fun getZIndex() = throw UnsupportedOperationException()

    override fun clipBegin() = throw UnsupportedOperationException()

    override fun clipBegin(x: Float, y: Float, width: Float, height: Float) = throw UnsupportedOperationException()

    override fun clipEnd() = throw UnsupportedOperationException()

    override fun screenToLocalCoordinates(screenCoords: Vector2?) = throw UnsupportedOperationException()

    override fun stageToLocalCoordinates(stageCoords: Vector2?) = throw UnsupportedOperationException()

    override fun parentToLocalCoordinates(parentCoords: Vector2?) = throw UnsupportedOperationException()

    override fun localToScreenCoordinates(localCoords: Vector2?) = throw UnsupportedOperationException()

    override fun localToStageCoordinates(localCoords: Vector2?) = throw UnsupportedOperationException()

    override fun localToParentCoordinates(localCoords: Vector2?) = throw UnsupportedOperationException()

    override fun localToAscendantCoordinates(ascendant: Actor?, localCoords: Vector2?) =
        throw UnsupportedOperationException()

    override fun localToActorCoordinates(actor: Actor?, localCoords: Vector2?) = throw UnsupportedOperationException()

    override fun drawDebug(shapes: ShapeRenderer?) = throw UnsupportedOperationException()

    override fun drawDebugBounds(shapes: ShapeRenderer?) = throw UnsupportedOperationException()

    override fun setDebug(enabled: Boolean) = throw UnsupportedOperationException()

    override fun getDebug() = throw UnsupportedOperationException()

    override fun debug() = throw UnsupportedOperationException()
}