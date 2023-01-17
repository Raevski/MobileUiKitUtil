package domain_layer.usecases.android

import domain_layer.usecases.CreateFileUseCase
import domain_layer.usecases.LoadComponents
import domain_layer.usecases.LoadImagesPathsUseCase
import domain_layer.usecases.MobileUtilUseCase
import kotlinx.coroutines.*
import network_layer.FigmaClient
import network_layer.models.nodes.Node
import network_layer.repositories.FigmaRepository
import java.io.File
import java.net.URI

class LoadAndGenerateComposeIcons(
    private val figmaRepository: FigmaRepository,
    private val figmaClient: FigmaClient
): MobileUtilUseCase<LoadAndGenerateComposeIcons.Params, Unit> {
    override suspend fun execute(params: Params) {
        val components = LoadComponents(figmaRepository, params.figmaFileHash).execute(null)

        val loadImagesPath = LoadImagesPathsUseCase(figmaRepository, params.figmaFileHash)
        val imagesMeta = loadImagesPath.execute(null)

        val nodes: Map<String, Node> =
            figmaRepository.getNodes(params.figmaFileHash, imagesMeta.keys.toList()).nodes

        val images = imagesMeta.values.toList()
        val createFileUseCase = CreateFileUseCase()

        try {
            images.map { imagePath ->
                val file = createFileUseCase.execute(
                    CreateFileUseCase.Params(
                        "src/main/res/drawables/",
                        getFileNameFromNode(imagePath, nodes, imagesMeta)
                    )
                )
                figmaClient.downloadFile(file, imagePath)
            }
        } catch (e: Throwable) {
            println("Failed loading")
        }
    }
    data class Params(val figmaFileHash: String, val resultFile: File)
}

fun getFileNameFromNode(imagePath: String, nodes: Map<String, Node>, imagesMeta: Map<String, String>): String {
    val imagesMetaItem = imagesMeta.entries.find { it.value == imagePath }

    return nodes[imagesMetaItem?.key]?.document?.name ?: imagePath
}

fun String.getFileNameFromUrl(): String {
    val uri = URI(this)
    val segments = uri.path.split("/")
    return segments[segments.size - 1]
}