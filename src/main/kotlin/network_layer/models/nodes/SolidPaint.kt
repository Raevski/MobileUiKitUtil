package network_layer.models.nodes

import kotlinx.serialization.Serializable

@Serializable
data class SolidPaint(val opacity: Double? = .0,
                      val color: PaintColor)
