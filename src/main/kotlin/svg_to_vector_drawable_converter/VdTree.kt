package svg_to_vector_drawable_converter

import svg_to_vector_drawable_converter.AssetUtil.newArgbBufferedImage
import java.awt.*
import java.awt.geom.Path2D
import java.awt.image.BufferedImage
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Used to represent the whole VectorDrawable XML file's tree.
 */
internal class VdTree {
    var mCurrentGroup = VdGroup()
    var mChildren: ArrayList<VdElement?>? = null
    var baseWidth = 1f
    var baseHeight = 1f
    var mPortWidth = 1f
    var mPortHeight = 1f
    var mRootAlpha = 1f

    /**
     * Ensure there is at least one animation for every path in group (linking
     * them by names) Build the "current" path based on the first group
     */
    fun parseFinish() {
        mChildren = mCurrentGroup.children
    }

    fun add(pathOrGroup: VdElement?) {
        mCurrentGroup.add(pathOrGroup)
    }

    private fun drawInternal(g: Graphics, w: Int, h: Int) {
        val scaleX = w / mPortWidth
        val scaleY = h / mPortHeight
        val minScale = Math.min(scaleX, scaleY)
        if (mChildren == null) {
            logger.log(Level.FINE, "no pathes")
            return
        }
        (g as Graphics2D).scale(scaleX.toDouble(), scaleY.toDouble())
        var bounds: Rectangle? = null
        for (i in mChildren!!.indices) {
            // TODO: do things differently when it is a path or group!!
            val path = mChildren!![i] as VdPath?
            logger.log(
                Level.FINE, "mCurrentPaths[" + i + "]=" + path!!.name +
                        Integer.toHexString(path.mFillColor)
            )
            if (mChildren!![i] != null) {
                val r = drawPath(path, g, w, h, minScale)
                if (bounds == null) {
                    bounds = r
                } else {
                    bounds.add(r)
                }
            }
        }
        logger.log(Level.FINE, "Rectangle $bounds")
        logger.log(Level.FINE, "Port  $mPortWidth,$mPortHeight")
        val right = mPortWidth - bounds!!.maxX
        val bot = mPortHeight - bounds.maxY
        logger.log(Level.FINE, "x " + bounds.minX + ", " + right)
        logger.log(Level.FINE, "y " + bounds.minY + ", " + bot)
    }

    private fun drawPath(path: VdPath?, canvas: Graphics, w: Int, h: Int, scale: Float): Rectangle {
        val path2d: Path2D = Path2D.Double()
        val g = canvas as Graphics2D
        path!!.toPath(path2d)
        // TODO: Use AffineTransform to apply group's transformation info.
        val theta = Math.toRadians(path.mRotate.toDouble())
        g.rotate(theta, path.mRotateX.toDouble(), path.mRotateY.toDouble())
        if (path.mClip) {
            logger.log(Level.FINE, "CLIP")
            g.color = Color.RED
            g.fill(path2d)
        }
        if (path.mFillColor != 0) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.color = Color(path.mFillColor, true)
            g.fill(path2d)
        }
        if (path.mStrokeColor != 0) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.stroke = BasicStroke(path.mStrokeWidth)
            g.color = Color(path.mStrokeColor, true)
            g.draw(path2d)
        }
        g.rotate(-theta, path.mRotateX.toDouble(), path.mRotateY.toDouble())
        return path2d.bounds
    }

    /**
     * Draw the VdTree into an image.
     * If the root alpha is less than 1.0, then draw into a temporary image,
     * then draw into the result image applying alpha blending.
     */
    fun drawIntoImage(image: BufferedImage) {
        val gFinal = image.graphics as Graphics2D
        val width = image.width
        val height = image.height
        gFinal.color = Color(255, 255, 255, 0)
        gFinal.fillRect(0, 0, width, height)
        val rootAlpha = mRootAlpha
        if (rootAlpha < 1.0) {
            val alphaImage = newArgbBufferedImage(width, height)
            val gTemp = alphaImage.graphics as Graphics2D
            drawInternal(gTemp, width, height)
            gFinal.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rootAlpha)
            gFinal.drawImage(alphaImage, 0, 0, null)
            gTemp.dispose()
        } else {
            drawInternal(gFinal, width, height)
        }
        gFinal.dispose()
    }

    companion object {
        private val logger = Logger.getLogger(VdTree::class.java.simpleName)
    }
}