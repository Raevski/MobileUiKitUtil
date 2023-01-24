package domain_layer.models

import config_dsl.replacedByToken
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import kotlin.math.roundToInt

data class Color(val name: String,
                 val originalName: String,
                 val red: Double,
                 val green: Double,
                 val blue: Double,
                 val alpha: Double) {
    fun inHex(): String {
        val redHex = "%02x".format((red * 255).roundToInt())
        val greenHex = "%02x".format((green * 255).roundToInt())
        val blueHex = "%02x".format((blue * 255).roundToInt())
        val alphaHex = "%02x".format((alpha * 255).roundToInt())
        return if (alpha > 0) {
            "#${alphaHex}${redHex}${greenHex}${blueHex}".uppercase()
        } else {
            "#${redHex}${greenHex}${blueHex}".uppercase()
        }
    }

    val redInt =(red * 255).roundToInt()

    val greenInt = (green * 255).roundToInt()

    val blueInt = (blue * 255).roundToInt()

    val alphaInt = (alpha * 255).roundToInt()

    private val prettiedName: String
        get() {
            return name.decapitalize().replacedByToken()
        }

    val nameForResource: String
        get() = prettiedName.replace("/", "_").lowercase()

    val nameForCode: String
        get() {
            return prettiedName
                .replace("/", "_")
                .split("_")
                .mapIndexed { index, word ->
                    return@mapIndexed if(index > 0) {
                        word.capitalizeAsciiOnly()
                    } else {
                        word
                    }
            }.joinToString("")
        }
}