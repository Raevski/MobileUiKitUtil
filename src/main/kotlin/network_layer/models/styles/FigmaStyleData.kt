package network_layer.models.styles

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FigmaStyleData(val key: String,
                          @SerialName("file_key")
                          val fileKey: String,
                          @SerialName("node_id")
                          val nodeId: String,
                          @SerialName("style_type")
                          val styleType: FigmaStyleType,
                          val description: String,
                          val name: String) {
    fun useStyle(): Boolean {
        if (description.isEmpty()) {
            return true
        }

        return !description.contains("none")
    }
}