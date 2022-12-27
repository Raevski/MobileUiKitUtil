package network_layer.models.nodes

import kotlinx.serialization.Serializable

@Serializable
data class Paint(val type: PaintType,
                 val opacity: Double? = null,
                 val color: PaintColor?)
