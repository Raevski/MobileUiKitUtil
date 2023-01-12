package domain_layer.models

import config_dsl.replacedByToken
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly

data class Color(val name: String,
                 val originalName: String,
                 val red: Double,
                 val green: Double,
                 val blue: Double,
                 val alpha: Double) {
    val prettiedName: String
        get() {
            return name.decapitalize().replacedByToken()
        }

    val nameForResource: String
        get() = prettiedName.replace("/", "_").lowercase()

    val nameForCode: String
        get() = prettiedName.replace("/", "")
}