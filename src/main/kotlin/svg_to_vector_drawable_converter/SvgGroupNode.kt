package svg_to_vector_drawable_converter

import org.w3c.dom.Node
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Represent a SVG file's group element.
 */
internal class SvgGroupNode(svgTree: SvgTree?, docNode: Node?, name: String?) : SvgNode(
    svgTree!!, docNode!!, name!!
) {
    private val mChildren = ArrayList<SvgNode>()
    fun addChild(child: SvgNode) {
        mChildren.add(child)
    }

    override fun dumpNode(indent: String?) {
        // Print the current group.
        logger.log(Level.FINE, indent + "current group is :" + name)
        // Then print all the children.
        for (node in mChildren) {
            node.dumpNode(indent + INDENT_LEVEL)
        }
    }

    override val isGroupNode: Boolean
        get() = true

    override fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float) {
        for (p in mChildren) {
            p.transform(a, b, c, d, e, f)
        }
    }

    @Throws(IOException::class)
    override fun writeXML(writer: OutputStreamWriter?) {
        for (node in mChildren) {
            node.writeXML(writer)
        }
    }

    companion object {
        private val logger = Logger.getLogger(SvgGroupNode::class.java.simpleName)
        private const val INDENT_LEVEL = "    "
    }
}