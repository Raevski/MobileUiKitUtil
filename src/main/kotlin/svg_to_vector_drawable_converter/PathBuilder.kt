package svg_to_vector_drawable_converter

internal class PathBuilder {
    private val mPathData = StringBuilder()
    private fun booleanToString(flag: Boolean): String {
        return if (flag) "1" else "0"
    }

    fun absoluteMoveTo(x: Float, y: Float): PathBuilder {
        mPathData.append("M$x,$y")
        return this
    }

    fun relativeMoveTo(x: Float, y: Float): PathBuilder {
        mPathData.append("m$x,$y")
        return this
    }

    fun absoluteLineTo(x: Float, y: Float): PathBuilder {
        mPathData.append("L$x,$y")
        return this
    }

    fun relativeLineTo(x: Float, y: Float): PathBuilder {
        mPathData.append("l$x,$y")
        return this
    }

    fun absoluteVerticalTo(v: Float): PathBuilder {
        mPathData.append("V$v")
        return this
    }

    fun relativeVerticalTo(v: Float): PathBuilder {
        mPathData.append("v$v")
        return this
    }

    fun absoluteHorizontalTo(h: Float): PathBuilder {
        mPathData.append("H$h")
        return this
    }

    fun relativeHorizontalTo(h: Float): PathBuilder {
        mPathData.append("h$h")
        return this
    }

    fun absoluteArcTo(
        rx: Float, ry: Float, rotation: Boolean,
        largeArc: Boolean, sweep: Boolean, x: Float, y: Float
    ): PathBuilder {
        mPathData.append(
            "A" + rx + "," + ry + "," + booleanToString(rotation) + "," +
                    booleanToString(largeArc) + "," + booleanToString(sweep) + "," + x + "," + y
        )
        return this
    }

    fun relativeArcTo(
        rx: Float, ry: Float, rotation: Boolean,
        largeArc: Boolean, sweep: Boolean, x: Float, y: Float
    ): PathBuilder {
        mPathData.append(
            "a" + rx + "," + ry + "," + booleanToString(rotation) + "," +
                    booleanToString(largeArc) + "," + booleanToString(sweep) + "," + x + "," + y
        )
        return this
    }

    fun absoluteClose(): PathBuilder {
        mPathData.append("Z")
        return this
    }

    fun relativeClose(): PathBuilder {
        mPathData.append("z")
        return this
    }

    override fun toString(): String {
        return mPathData.toString()
    }
}