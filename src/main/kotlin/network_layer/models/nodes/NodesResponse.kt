package network_layer.models.nodes

import kotlinx.serialization.Serializable

@Serializable
data class NodesResponse(val nodes: Map<String, Node>)
