package network_layer.models.images

import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    val meta: ImagesMeta = ImagesMeta()
)