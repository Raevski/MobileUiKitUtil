package network_layer.models.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComponentData(val key: String,
                         @SerialName("file_key")
                         val fileKey: String,
                         @SerialName("node_id")
                         val nodeId: String,
                         @SerialName("thumbnail_url")
                         val thumbnailUrl: String,
                         val name: String,
                         val description: String,
                         @SerialName("containing_frame")
                         val containingFrame: ComponentContainingFrame)