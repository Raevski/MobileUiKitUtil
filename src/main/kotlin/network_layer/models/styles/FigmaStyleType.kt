package network_layer.models.styles

import kotlinx.serialization.Serializable
import network_layer.models.nodes.LineHeightUnitSerializer

@Serializable
enum class FigmaStyleType {
    FILL,
    TEXT,
    EFFECT,
    GRID;
}