package domain_layer.models

data class UtilParams(
    val platform: Platform,
    val resourceType: ExportResourceType,
    val figmaToken: String,
    val fileHash: String,
    val isLogging: Boolean,
    val resourcesResultPath: String,
    val classesResultPath: String,
    val nodeId: String,
    val pageName: String,
    val showkaseEnabled: Boolean,
    val resultClassName: String,
    val resultPackageName: String
) {
    companion object {

        //This function can be used to fast executing with new params from source code
        //For common usages just use ParseConfigUseCase to build params
        fun createParamsFromArgs(args: Array<String>): UtilParams {
            val figmaToken = args.getOrElse(0) { "" }
            val fileHash = args.getOrElse(1) { "" }

            val platform = args.getOrElse(2) { "" }.getPlatform()
            val resourceType = args.getOrElse(3) { "" }.getExportResourceType()
            val isLoggingParam = args.get(4).orEmpty()
            val resourcesfilePath = args.get(5).orEmpty()
            val nodeId = args.get(6).orEmpty()
            val pageName = args.get(7).orEmpty()
            val showkaseEnabledParam = args.get(8).orEmpty()
            val resultClassName = args.get(9).orEmpty()
            val resultPackageName = args.get(10).orEmpty()
            val classesResultPath = args.get(11).orEmpty()

            val isLogging = if (isLoggingParam.isEmpty()) {
                false
            } else {
                isLoggingParam.toBoolean()
            }

            val showkaseEnabled = if (showkaseEnabledParam.isEmpty()) {
                false
            }  else {
                showkaseEnabledParam.toBoolean()
            }

            return UtilParams(platform,
                resourceType,
                figmaToken,
                fileHash,
                isLogging,
                resourcesfilePath,
                classesResultPath,
                nodeId,
                pageName,
                showkaseEnabled,
                resultClassName,
                resultPackageName)
        }
    }
}