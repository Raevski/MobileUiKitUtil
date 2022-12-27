package domain_layer

enum class ExportCommand(val commandString: String) {
    TYPOGRAPHY("typography"),
    COLORS("colors"),
    ICONS("icons")
}

fun String.getExportCommand(): ExportCommand {
    return ExportCommand::commandString.find(this)
        ?: throw IllegalArgumentException("Bad platform string: $this")
}

inline fun <reified T : Enum<T>, V> ((T) -> V).find(value: V): T? {
    return enumValues<T>().firstOrNull { this(it) == value }
}