package domain_layer.models

import domain_layer.*

data class UtilParams(
    val platform: Platform,
    val resourceType: ExportResourceType,
    val figmaToken: String,
    val fileHash: String,
    val isLogging: Boolean,
    val resultPath: String
) {
    companion object {
        fun createParamsFromArgs(args: Array<String>): UtilParams {
            val figmaToken = args.getOrElse(0) { "" }
            val fileHash = args.getOrElse(1) { "" }

            val platform = args.getOrElse(2) { "" }.getPlatform()
            val resourceType = args.getOrElse(3) { "" }.getExportResourceType()
            val isLoggingParam = args.get(4).orEmpty()
            val filePath = args.get(5).orEmpty()

            var isLogging = false

            if (isLoggingParam.isEmpty()) {
                isLogging = false
            } else {
                isLogging = isLoggingParam.toBoolean()
            }

            return UtilParams(platform, resourceType, figmaToken, fileHash, isLogging, filePath)
        }
    }
}