package config_dsl

import domain_layer.models.ExportResourceType
import domain_layer.models.Platform
import domain_layer.models.getExportResourceType
import domain_layer.models.getPlatform

class MobileFigmaExportConfig {
    var figmaToken: String = ""
    var platform: String = ""
    var resourceType: String = ""
    var fileHash: String = ""
    var isLogging: Boolean = true
    var resultPath: String = ""
    var tokens: Tokenization? = null
    val tokenizationBuilder = TokenizationBuilder()

    val targetPlatform: Platform
        get() = platform.getPlatform()

    val targetResourceType: ExportResourceType
        get() = resourceType.getExportResourceType()

    fun tokenization(init: TokenizationBuilder.() -> Unit) {
        tokenizationBuilder.init()
    }

    constructor(figmaToken: String = "",
                platform: Platform = Platform.ANDROID,
                resourceType: ExportResourceType = ExportResourceType.TYPOGRAPHY,
                fileHash: String = "",
                isLogging: Boolean = true,
                resultPath: String = "",
                tokens: Tokenization? = null) {
        this.figmaToken = figmaToken
        this.platform = platform.platformString
        this.resourceType = resourceType.resourceTypeString
        this.fileHash = fileHash
        this.isLogging = isLogging
        this.resultPath = resultPath
        this.tokens = tokens
    }

    constructor(initializer: MobileFigmaExportConfig.() -> Unit) {
        this.initializer()
    }
}