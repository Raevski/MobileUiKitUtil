package network_layer.models.components

import kotlinx.serialization.Serializable

@Serializable
data class ComponentContainingFrame(val name: String? = null,
                                    val nodeId: String? = null,
                                    val pageId: String,
                                    val pageName: String)