package network_layer.models.components

import kotlinx.serialization.Serializable

@Serializable
data class ComponentContainingFrame(val name: String,
                                    val nodeId: String,
                                    val pageId: String,
                                    val pageName: String)