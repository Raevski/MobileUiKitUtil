package config_dsl

data class Tokenization(val specific: Map<String, String>, val default: String)

class TokenizationBuilder {
    var default = ""
    private val specific = HashMap<String, String>()

    infix fun String.to (target: String) {
        specific[target] = this
    }

    fun build(): Tokenization { return Tokenization(specific, default) }
}
