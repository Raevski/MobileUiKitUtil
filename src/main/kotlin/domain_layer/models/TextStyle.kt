package domain_layer.models

data class TextStyle(
    val name: String = "",
    val fontName: String = "",
    val fontSize: Int = 0,
    val fontStyle: String = "",
    val lineHeight: Double = .0,
    val letterSpacing: Double = .0,
    val textCase: String = ""
)
