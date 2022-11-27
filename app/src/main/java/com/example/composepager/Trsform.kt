package com.example.composepager

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import kotlin.math.abs

class CustomGraphicsLayerScope(
    override var scaleX: Float = 1f,
    override var scaleY: Float = 1f,
    override var alpha: Float = 1f,
    override var translationX: Float = 0f,
    override var translationY: Float = 0f
) : GraphicsLayerScope {
    override var shadowElevation: Float = 0f
    override var ambientShadowColor: Color = DefaultShadowColor
    override var spotShadowColor: Color = DefaultShadowColor
    override var rotationX: Float = 0f
    override var rotationY: Float = 0f
    override var rotationZ: Float = 0f
    override var cameraDistance: Float = DefaultCameraDistance
    override var transformOrigin: TransformOrigin = TransformOrigin.Center
    override var shape: Shape = RectangleShape
    override var clip: Boolean = false
    override var compositingStrategy: CompositingStrategy = CompositingStrategy.Auto
    override var size: Size = Size.Zero

    private var graphicsDensity: Density = Density(1.0f)

    override val density: Float
        get() = graphicsDensity.density

    override val fontScale: Float
        get() = graphicsDensity.fontScale

    override var renderEffect: RenderEffect? = null

    fun reset() {
        scaleX = 1f
        scaleY = 1f
        alpha = 1f
        translationX = 0f
        translationY = 0f
        shadowElevation = 0f
        ambientShadowColor = DefaultShadowColor
        spotShadowColor = DefaultShadowColor
        rotationX = 0f
        rotationY = 0f
        rotationZ = 0f
        cameraDistance = DefaultCameraDistance
        transformOrigin = TransformOrigin.Center
        shape = RectangleShape
        clip = false
        renderEffect = null
        compositingStrategy = CompositingStrategy.Auto
        size = Size.Zero
    }
}


private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

fun zoomOutTransformer(position: Float, width: Float, height: Float): GraphicsLayerScope {
    return CustomGraphicsLayerScope().apply {
        if (position < -1) {
            alpha = 0f
        } else if (position < 1) {
            val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
            val vertMargin = height * (1 - scaleFactor) / 2
            val horzMargin = width * (1 - scaleFactor) / 2
            translationX = if (position < 0) {
                horzMargin - vertMargin / 2
            } else {
                horzMargin + vertMargin / 2
            }
            scaleX = scaleFactor
            scaleY = scaleFactor
            alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
        } else {
            alpha = 0f
        }
    }
}

fun depthTransformer(position: Float, width: Float): GraphicsLayerScope {
    return CustomGraphicsLayerScope().apply {
        if (position < -1) {
            alpha = 0f
        } else if (position <= 0) {
            alpha = 1f
            translationX = 0f
            scaleX = 1f
            scaleY = 1f
        } else if (position <= 1) {
            alpha = 1 - position

            translationX = width * -position

            val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position)))
            scaleX = scaleFactor
            scaleY = scaleFactor
        }else {
            alpha = 0f
        }
    }
}

fun GraphicsLayerScope.setFrom(graphicsLayerScope: GraphicsLayerScope) {
    alpha = graphicsLayerScope.alpha
    scaleX = graphicsLayerScope.scaleX
    scaleY = graphicsLayerScope.scaleY
    translationX = graphicsLayerScope.translationX
    translationY = graphicsLayerScope.translationY
}


