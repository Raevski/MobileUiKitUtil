package network_layer.models.nodes

import kotlinx.serialization.Serializable

@Serializable
data class TypeStyle(
    val fontFamily: String = "",
    val fontPostScriptName: String = "",
    val fontWeight: Double,
    val fontSize: Double,
    val lineHeightPx: Double,
    val letterSpacing: Double,
    val lineHeightUnit: LineHeightUnit,
    val textCase: TextCase
)
