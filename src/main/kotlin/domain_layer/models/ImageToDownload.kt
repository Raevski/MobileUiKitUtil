package domain_layer.models

import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

data class ImageToDownload(val nodeId: String,
                           val path: String,
                           val imageFileName: String,
                           val assetName: String) {
    fun composeIconName(): String {
        val splittedWords = assetName.split("_")
        splittedWords.forEachIndexed { index, word ->
            return if(index > 0) {
                word.capitalizeAsciiOnly()
            } else {
                word
            }
        }
        return splittedWords.joinToString()
    }

}