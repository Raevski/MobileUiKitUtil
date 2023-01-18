package domain_layer.models

data class ImageToDownload(val nodeId: String,
                           val path: String,
                           val imageFileName: String,
                           val assetName: String)