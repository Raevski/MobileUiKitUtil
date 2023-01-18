package svg_to_vector_drawable_converter

import org.w3c.dom.Node
import java.io.IOException
import java.io.OutputStreamWriter

/**
 * Parent class for a SVG file's node, can be either group or leave element.
 */
internal abstract class SvgNode(// Keep a reference to the tree in order to dump the error log.
    protected val tree: SvgTree, // Use document node to get the line number for error reporting.
    val documentNode: Node, var name: String
) {

    /**
     * dump the current node's debug info.
     */
    abstract fun dumpNode(indent: String?)

    /**
     * Write the Node content into the VectorDrawable's XML file.
     */
    @Throws(IOException::class)
    abstract fun writeXML(writer: OutputStreamWriter?)

    /**
     * @return true the node is a group node.
     */
    abstract val isGroupNode: Boolean

    /**
     * Transform the current Node with the transformation matrix.
     */
    abstract fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float)
}