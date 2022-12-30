package network_layer.models.nodes

import kotlinx.serialization.Serializable

@Serializable
data class Document(val id: String,
                    val name: String,
                    val fills: List<Paint>,
                    val style: TypeStyle? = null)
