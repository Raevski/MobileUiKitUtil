package network_layer.models.styles

import kotlinx.serialization.Serializable

@Serializable
data class FigmaStylesMeta(val styles: List<FigmaStyleData> = listOf())