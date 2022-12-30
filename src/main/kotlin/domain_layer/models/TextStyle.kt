package domain_layer.models

import network_layer.models.nodes.TextCase

data class TextStyle(
    val name: String = "",
    val fontName: String = "",
    val fontSize: Double = .0,
    val fontStyle: String = "",
    val lineHeight: Double = .0,
    val letterSpacing: Double = .0,
    val textCase: TextCase = TextCase.ORIGINAL,
    val fontWeight: Double = 500.0
)
