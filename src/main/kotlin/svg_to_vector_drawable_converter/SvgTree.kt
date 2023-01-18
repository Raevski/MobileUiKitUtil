package svg_to_vector_drawable_converter

import com.google.common.base.Strings
import org.w3c.dom.Document
import org.w3c.dom.Node
import svg_to_vector_drawable_converter.PositionXmlParser.parse
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Represent the SVG file in an internal data structure as a tree.
 */
internal class SvgTree {
    var w = 0f
    var h = 0f
    var matrix: FloatArray?
    var viewBox: FloatArray?
    var mScaleFactor = 1f
    var root: SvgGroupNode? = null
    private var mFileName: String? = null
    private val mErrorLines = ArrayList<String>()

    enum class SvgLogLevel {
        ERROR, WARNING
    }

    @Throws(Exception::class)
    fun parse(f: File): Document {
        mFileName = f.name
        return parse(FileInputStream(f), false)
    }

    fun normalize() {
        if (matrix != null) {
            transform(matrix!![0], matrix!![1], matrix!![2], matrix!![3], matrix!![4], matrix!![5])
        }
        if (viewBox != null && (viewBox!![0] != 0f || viewBox!![1] != 0f)) {
            transform(1f, 0f, 0f, 1f, -viewBox!![0], -viewBox!![1])
        }
        logger.log(Level.FINE, "matrix=" + Arrays.toString(matrix))
    }

    private fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float) {
        root!!.transform(a, b, c, d, e, f)
    }

    fun dump(root: SvgGroupNode) {
        logger.log(Level.FINE, "current file is :$mFileName")
        root.dumpNode("")
    }

    fun logErrorLine(s: String, node: Node?, level: SvgLogLevel) {
        if (!Strings.isNullOrEmpty(s)) {
            if (node != null) {
                val position = getPosition(node)
                mErrorLines.add(
                    """${level.name}@ line ${position.startLine + 1} $s
"""
                )
            } else {
                mErrorLines.add(s)
            }
        }
    }

    val errorLog: String
        /**
         * @return Error log. Empty string if there are no errors.
         */
        get() {
            val errorBuilder = StringBuilder()
            if (!mErrorLines.isEmpty()) {
                errorBuilder.append("In $mFileName:\n")
            }
            for (log in mErrorLines) {
                errorBuilder.append(log)
            }
            return errorBuilder.toString()
        }

    /**
     * @return true when there is no error found when parsing the SVG file.
     */
    fun canConvertToVectorDrawable(): Boolean {
        return mErrorLines.isEmpty()
    }

    private fun getPosition(node: Node): SourcePosition {
        return PositionXmlParser.getPosition(node)
    }

    companion object {
        private val logger = Logger.getLogger(SvgTree::class.java.simpleName)
    }
}