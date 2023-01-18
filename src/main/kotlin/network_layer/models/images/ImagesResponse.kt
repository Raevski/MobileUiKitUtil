package network_layer.models.images

import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    val images: Map<String, String> = mapOf()
)