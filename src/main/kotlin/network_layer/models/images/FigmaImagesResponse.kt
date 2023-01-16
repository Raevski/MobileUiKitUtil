package network_layer.models.images

import kotlinx.serialization.Serializable

@Serializable
data class FigmaImagesResponse(
    val images: Map<Long, String> = mapOf()
)