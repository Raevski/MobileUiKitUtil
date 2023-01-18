package svg_to_vector_drawable_converter

import com.google.common.base.Objects
import jdk.nashorn.internal.ir.annotations.Immutable

/**
 * An immutable position in a text file, used in errors to point the user to an issue.
 *
 * Positions that are unknown are represented by -1.
 */
@Immutable
class SourcePosition {
    val startLine: Int
    val startColumn: Int
    val startOffset: Int
    val endLine: Int
    val endColumn: Int
    val endOffset: Int

    constructor(
        startLine: Int, startColumn: Int, startOffset: Int,
        endLine: Int, endColumn: Int, endOffset: Int
    ) {
        this.startLine = startLine
        this.startColumn = startColumn
        this.startOffset = startOffset
        this.endLine = endLine
        this.endColumn = endColumn
        this.endOffset = endOffset
    }

    constructor(lineNumber: Int, column: Int, offset: Int) {
        endLine = lineNumber
        startLine = endLine
        endColumn = column
        startColumn = endColumn
        endOffset = offset
        startOffset = endOffset
    }

    private constructor() {
        endOffset = -1
        endColumn = endOffset
        endLine = endColumn
        startOffset = endLine
        startColumn = startOffset
        startLine = startColumn
    }

    protected constructor(copy: SourcePosition) {
        startLine = copy.startLine
        startColumn = copy.startColumn
        startOffset = copy.startOffset
        endLine = copy.endLine
        endColumn = copy.endColumn
        endOffset = copy.endOffset
    }

    /**
     * Outputs positions as human-readable formatted strings.
     *
     * e.g.
     * <pre>84
     * 84-86
     * 84:5
     * 84:5-28
     * 85:5-86:47</pre>
     *
     * @return a human readable position.
     */
    override fun toString(): String {
        if (startLine == -1) {
            return "?"
        }
        val sB = StringBuilder(15)
        sB.append(startLine + 1) // Humans think that the first line is line 1.
        if (startColumn != -1) {
            sB.append(':')
            sB.append(startColumn + 1)
        }
        if (endLine != -1) {
            if (endLine == startLine) {
                if (endColumn != -1 && endColumn != startColumn) {
                    sB.append('-')
                    sB.append(endColumn + 1)
                }
            } else {
                sB.append('-')
                sB.append(endLine + 1)
                if (endColumn != -1) {
                    sB.append(':')
                    sB.append(endColumn + 1)
                }
            }
        }
        return sB.toString()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj !is SourcePosition) {
            return false
        }
        val other = obj
        return other.startLine == startLine && other.startColumn == startColumn && other.startOffset == startOffset && other.endLine == endLine && other.endColumn == endColumn && other.endOffset == endOffset
    }

    override fun hashCode(): Int {
        return Objects
            .hashCode(startLine, startColumn, startOffset, endLine, endColumn, endOffset)
    }

    companion object {
        val UNKNOWN = SourcePosition()
    }
}