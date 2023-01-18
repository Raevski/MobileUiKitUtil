package svg_to_vector_drawable_converter

import java.awt.geom.Path2D
import java.util.*

/**
 * Used to represent one VectorDrawble's path element.
 */
internal class VdPath : VdElement() {
    var mNode: Array<Node>? = null
    var mStrokeColor = 0
    var mFillColor = 0
    var mStrokeWidth = 0f
    var mRotate = 0f
    var mShiftX = 0f
    var mShiftY = 0f
    var mRotateX = 0f
    var mRotateY = 0f
    var trimPathStart = 0f
    var trimPathEnd = 1f
    var trimPathOffset = 0f
    var mStrokeLineCap = -1
    var mStrokeLineJoin = -1
    var mStrokeMiterlimit = -1f
    var mClip = false
    var mStrokeOpacity = Float.NaN
    var mFillOpacity = Float.NaN
    var mTrimPathStart = 0f
    var mTrimPathEnd = 1f
    var mTrimPathOffset = 0f
    fun toPath(path: Path2D) {
        path.reset()
        if (mNode != null) {
            VdNodeRender.creatPath(mNode, path)
        }
    }

    class Node {
        var type: Char
        var params: FloatArray

        constructor(type: Char, params: FloatArray) {
            this.type = type
            this.params = params
        }

        constructor(n: Node) {
            type = n.type
            params = Arrays.copyOf(n.params, n.params.size)
        }

        fun transform(
            a: Float,
            b: Float,
            c: Float,
            d: Float,
            e: Float,
            f: Float,
            pre: FloatArray
        ) {
            var incr = 0
            val tempParams: FloatArray
            val origParams: FloatArray
            when (type) {
                'z', 'Z' -> return
                'M', 'L', 'T' -> {
                    incr = 2
                    pre[0] = params[params.size - 2]
                    pre[1] = params[params.size - 1]
                    var i = 0
                    while (i < params.size) {
                        matrix(a, b, c, d, e, f, i, i + 1)
                        i += incr
                    }
                }

                'm', 'l', 't' -> {
                    incr = 2
                    pre[0] += params[params.size - 2]
                    pre[1] += params[params.size - 1]
                    var i = 0
                    while (i < params.size) {
                        matrix(a, b, c, d, 0f, 0f, i, i + 1)
                        i += incr
                    }
                }

                'h' -> {
                    type = 'l'
                    pre[0] += params[params.size - 1]
                    tempParams = FloatArray(params.size * 2)
                    origParams = params
                    params = tempParams
                    var i = 0
                    while (i < params.size) {
                        params[i] = origParams[i / 2]
                        params[i + 1] = 0f
                        matrix(a, b, c, d, 0f, 0f, i, i + 1)
                        i += 2
                    }
                }

                'H' -> {
                    type = 'L'
                    pre[0] = params[params.size - 1]
                    tempParams = FloatArray(params.size * 2)
                    origParams = params
                    params = tempParams
                    var i = 0
                    while (i < params.size) {
                        params[i] = origParams[i / 2]
                        params[i + 1] = pre[1]
                        matrix(a, b, c, d, e, f, i, i + 1)
                        i += 2
                    }
                }

                'v' -> {
                    pre[1] += params[params.size - 1]
                    type = 'l'
                    tempParams = FloatArray(params.size * 2)
                    origParams = params
                    params = tempParams
                    var i = 0
                    while (i < params.size) {
                        params[i] = 0f
                        params[i + 1] = origParams[i / 2]
                        matrix(a, b, c, d, 0f, 0f, i, i + 1)
                        i += 2
                    }
                }

                'V' -> {
                    type = 'L'
                    pre[1] = params[params.size - 1]
                    tempParams = FloatArray(params.size * 2)
                    origParams = params
                    params = tempParams
                    var i = 0
                    while (i < params.size) {
                        params[i] = pre[0]
                        params[i + 1] = origParams[i / 2]
                        matrix(a, b, c, d, e, f, i, i + 1)
                        i += 2
                    }
                }

                'C', 'S', 'Q' -> {
                    pre[0] = params[params.size - 2]
                    pre[1] = params[params.size - 1]
                    var i = 0
                    while (i < params.size) {
                        matrix(a, b, c, d, e, f, i, i + 1)
                        i += 2
                    }
                }

                's', 'q', 'c' -> {
                    pre[0] += params[params.size - 2]
                    pre[1] += params[params.size - 1]
                    var i = 0
                    while (i < params.size) {
                        matrix(a, b, c, d, 0f, 0f, i, i + 1)
                        i += 2
                    }
                }

                'a' -> {
                    incr = 7
                    pre[0] += params[params.size - 2]
                    pre[1] += params[params.size - 1]
                    var i = 0
                    while (i < params.size) {
                        matrix(a, b, c, d, 0f, 0f, i, i + 1)
                        val ang = Math.toRadians(params[i + 2].toDouble())
                        params[i + 2] = Math.toDegrees(ang + Math.atan2(b.toDouble(), d.toDouble())).toFloat()
                        matrix(a, b, c, d, 0f, 0f, i + 5, i + 6)
                        i += incr
                    }
                }

                'A' -> {
                    incr = 7
                    pre[0] = params[params.size - 2]
                    pre[1] = params[params.size - 1]
                    var i = 0
                    while (i < params.size) {
                        matrix(a, b, c, d, e, f, i, i + 1)
                        val ang = Math.toRadians(params[i + 2].toDouble())
                        params[i + 2] = Math.toDegrees(ang + Math.atan2(b.toDouble(), d.toDouble())).toFloat()
                        matrix(a, b, c, d, e, f, i + 5, i + 6)
                        i += incr
                    }
                }
            }
        }

        fun matrix(
            a: Float,
            b: Float,
            c: Float,
            d: Float,
            e: Float,
            f: Float,
            offx: Int,
            offy: Int
        ) {
            val inx: Float = if (offx < 0) 1 else params[offx]
            val iny: Float = if (offy < 0) 1 else params[offy]
            val x = inx * a + iny * c + e
            val y = inx * b + iny * d + f
            if (offx >= 0) {
                params[offx] = x
            }
            if (offy >= 0) {
                params[offy] = y
            }
        }

        companion object {
            fun NodeListToString(nodes: Array<Node>): String {
                var s = ""
                for (i in nodes.indices) {
                    val n = nodes[i]
                    s += n.type
                    val len = n.params.size
                    for (j in 0 until len) {
                        if (j > 0) {
                            s += if (j and 1 == 1) "," else " "
                        }
                        // To avoid trailing zeros like 17.0, use this trick
                        val value = n.params[j]
                        s += if (value == value.toLong().toFloat()) {
                            value.toLong().toString()
                        } else {
                            value.toString()
                        }
                    }
                }
                return s
            }

            fun transform(
                a: Float,
                b: Float,
                c: Float,
                d: Float,
                e: Float,
                f: Float,
                nodes: Array<Node>?
            ) {
                val pre = FloatArray(2)
                for (i in nodes!!.indices) {
                    nodes[i].transform(a, b, c, d, e, f, pre)
                }
            }
        }
    }

    init {
        mName = this.toString() // to ensure paths have unique names
    }

    /**
     * TODO: support rotation attribute for stroke width
     */
    fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float) {
        mStrokeWidth *= Math.hypot((a + b).toDouble(), (c + d).toDouble()).toFloat()
        Node.transform(a, b, c, d, e, f, mNode)
    }
}