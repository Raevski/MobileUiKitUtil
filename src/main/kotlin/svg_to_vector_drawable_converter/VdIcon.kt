package svg_to_vector_drawable_converter

import svg_to_vector_drawable_converter.AssetUtil.drawCenterInside
import svg_to_vector_drawable_converter.AssetUtil.newArgbBufferedImage
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.net.URL
import javax.swing.Icon

/**
 * VdIcon wrap every vector drawable from Material Library into an icon.
 * All of them are shown in a table for developer to pick.
 */
class VdIcon(url: URL) : Icon, Comparable<VdIcon> {
    private var mVdTree: VdTree? = null
    val name: String
    val uRL: URL

    init {
        setDynamicIcon(url)
        uRL = url
        val fileName = url.file
        name = fileName.substring(fileName.lastIndexOf("/") + 1)
    }

    fun setDynamicIcon(url: URL) {
        val p = VdParser()
        try {
            mVdTree = p.parse(url.openStream(), null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        // We knew all the icons from Material library are square shape.
        val minSize = Math.min(c.width, c.height)
        val image = newArgbBufferedImage(minSize, minSize)
        mVdTree!!.drawIntoImage(image)
        // Draw in the center of the component.
        val rect = Rectangle(0, 0, c.width, c.height)
        drawCenterInside((g as Graphics2D), image, rect)
    }

    override fun getIconWidth(): Int {
        return (if (mVdTree != null) mVdTree!!.mPortWidth else 0).toInt()
    }

    override fun getIconHeight(): Int {
        return (if (mVdTree != null) mVdTree!!.mPortHeight else 0).toInt()
    }

    override fun compareTo(other: VdIcon): Int {
        return name.compareTo(other.name)
    }
}