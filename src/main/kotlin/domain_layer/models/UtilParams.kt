package domain_layer.models

import domain_layer.*

data class UtilParams(val platform: Platform,
                      val command: ExportCommand,
                      val figmaToken: String,
                      val fileHash: String) {
    companion object {
        fun createParamsFromArgs(args: Array<String>): UtilParams {
            val figmaToken = args.getOrElse(0) { "" }
            val fileHash = args.getOrElse(1) { "" }

            val platform = args.getOrElse(2) { "" }.getPlatform()
            val command = args.getOrElse(3) { "" }.getExportCommand()

            return UtilParams(platform, command, figmaToken, fileHash)
        }
    }
}