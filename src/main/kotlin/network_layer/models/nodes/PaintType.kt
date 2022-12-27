package network_layer.models.nodes

import kotlinx.serialization.Serializable

@Serializable
enum class PaintType {
    SOLID,
    IMAGE,
    RECTANGLE,
    GRADIENT_LINEAR,
    GRADIENT_RADIAL,
    GRADIENT_ANGULAR,
    GRADIENT_DIAMOND
}