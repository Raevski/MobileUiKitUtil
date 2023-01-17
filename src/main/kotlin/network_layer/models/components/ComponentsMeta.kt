package network_layer.models.components

import kotlinx.serialization.Serializable

@Serializable
data class ComponentsMeta(val components: List<ComponentData>)