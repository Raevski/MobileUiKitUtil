package network_layer.models.nodes

import kotlinx.serialization.Serializable

@Serializable
data class PaintColor(val r: Double, val g: Double, val b: Double, val a: Double)
