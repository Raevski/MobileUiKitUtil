package domain_layer.models

import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

data class ImageToDownload(val nodeId: String,
                           val path: String,
                           val imageFileName: String,
                           val assetName: String) {
    fun composeIconName(): String {

        return assetName.split("_").mapIndexed { index, word ->
            return@mapIndexed if(index > 0) {
                word.capitalizeAsciiOnly()
            } else {
                word
            }
        }.joinToString("")
    }

}