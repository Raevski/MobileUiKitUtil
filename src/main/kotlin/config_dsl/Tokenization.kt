package config_dsl

object Tokenization {
    var specific: Map<String, String> = mapOf()
    var default: String = ""
}

fun String.replacedByToken(): String {
    if (Tokenization.specific.containsKey(this)) {
        return Tokenization.specific[this]!!
    }

    return this
}

class TokenizationBuilder {
    var default = ""
    private val specific = HashMap<String, String>()

    infix fun String.to (target: String) {
        specific[target] = this
    }

    fun build(): Tokenization {
        Tokenization.specific = this.specific
        return Tokenization
    }
}
