package network_layer.models.styles

import kotlinx.serialization.Serializable

@Serializable
data class FigmaStylesResponse(val error: Boolean = false,
                               val status: Int = 0,
                               val meta: FigmaStylesMeta = FigmaStylesMeta()
)