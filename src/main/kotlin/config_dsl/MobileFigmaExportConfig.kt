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
    var resourcesResultPath: String = ""
    var classesResultPath: String = ""
    var nodeId: String = ""
    var pageName: String = ""
    var showkaseEnabled: Boolean = false
    var resultClassName: String = ""
    var resultPackageName: String = ""
    val tokenizationBuilder = TokenizationBuilder()

    val targetPlatform: Platform
        get() = platform.getPlatform()

    val targetResourceType: ExportResourceType
        get() = resourceType.getExportResourceType()

    fun tokenization(init: TokenizationBuilder.() -> Unit) {
        tokenizationBuilder.init()
        tokenizationBuilder.build()
    }

    constructor(figmaToken: String = "",
                platform: Platform = Platform.ANDROID,
                resourceType: ExportResourceType = ExportResourceType.TYPOGRAPHY,
                fileHash: String = "",
                isLogging: Boolean = true,
                resourcesResultPath: String = "",
                nodeId: String = "",
                pageName: String = "",
                showkaseEnabled: Boolean = false,
                resultClassName: String,
                resultPackageName: String,
                classesResultPath: String) {
        this.figmaToken = figmaToken
        this.platform = platform.platformString
        this.resourceType = resourceType.resourceTypeString
        this.fileHash = fileHash
        this.isLogging = isLogging
        this.resourcesResultPath = resourcesResultPath
        this.classesResultPath = classesResultPath
        this.nodeId = nodeId
        this.pageName = pageName
        this.showkaseEnabled = showkaseEnabled
        this.resultClassName = resultClassName
        this.resultPackageName = resultPackageName
    }

    constructor(initializer: MobileFigmaExportConfig.() -> Unit) {
        this.initializer()
    }
}