package config_dsl

data class Config(val figmaToken: String, val tokens: Tokenization) {
    companion object {
        fun mobileFigmaUtilConfig(init: ConfigBuilder.() -> Unit): Config {
            val builder = ConfigBuilder()
            builder.init()
            return builder.build()
        }
    }
}

class ConfigBuilder {
    var figmaToken = ""
    private val tokenizationBuilder = TokenizationBuilder()

    fun tokenization(init: TokenizationBuilder.() -> Unit) {
        tokenizationBuilder.init()
    }

    fun build(): Config { return Config(this.figmaToken, this.tokenizationBuilder.build()) }
}