package domain_layer.models

enum class ExportResourceType(val resourceTypeString: String) {
    TYPOGRAPHY("Typography"),
    COLORS("Colors"),
    ICONS("Icons")
}

fun String.getExportResourceType(): ExportResourceType {
    return ExportResourceType::resourceTypeString.find(this)
        ?: throw IllegalArgumentException("Bad resource type: $this")
}

inline fun <reified T : Enum<T>, V> ((T) -> V).find(value: V): T? {
    return enumValues<T>().firstOrNull { this(it) == value }
}