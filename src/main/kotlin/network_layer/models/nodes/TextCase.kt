package network_layer.models.nodes

import kotlinx.serialization.Serializable

@Serializable
enum class TextCase {
    ORIGINAL,
    UPPER,
    LOWER,
    TITLE,
    SMALL_CAPS,
    SMALL_CAPS_FORCED
}