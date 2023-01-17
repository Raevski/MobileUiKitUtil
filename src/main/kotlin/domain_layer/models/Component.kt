package domain_layer.models

import kotlinx.serialization.SerialName
import network_layer.models.components.ComponentContainingFrame

data class Component(val key: String,
                     val fileKey: String,
                     val nodeId: String,
                     val thumbnailUrl: String,
                     val name: String,
                     val description: String,
                     val frameName: String,
                     val frameNodeId: String,
                     val pageId: String,
                     val pageName: String) {
    val clearedName: String =
        name.replace("/", "_").lowercase()


    val pngImageName = "$clearedName.png"
}