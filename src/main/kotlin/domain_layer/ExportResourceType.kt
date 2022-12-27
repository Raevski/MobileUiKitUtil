package domain_layer

enum class ExportResourceType(val resourceTypeString: String) {
    TYPOGRAPHY("typography"),
    COLORS("colors"),
    ICONS("icons")
}

fun String.getExportResourceType(): ExportResourceType {
    return ExportResourceType::resourceTypeString.find(this)
        ?: throw IllegalArgumentException("Bad resource type: $this")
}

inline fun <reified T : Enum<T>, V> ((T) -> V).find(value: V): T? {
    return enumValues<T>().firstOrNull { this(it) == value }
}