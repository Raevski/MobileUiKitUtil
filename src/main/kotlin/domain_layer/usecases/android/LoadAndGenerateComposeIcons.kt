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
        var components = LoadComponents(figmaRepository, params.figmaFileHash).execute(null)

        if (params.pageName != null) {
            components = components.filter { it.pageName == params.pageName }
        }

        if (components.isEmpty()) {
            println("No page with this name ${params.pageName}")

            return
        }

        if (params.nodeId != null) {
            components = components.filter { it.nodeId == params.nodeId }
        }

        if (components.isEmpty()) {
            println("No node with such nodeId ${params.nodeId}")

            return
        }

        /*val loadImagesPath = LoadImagesPathsUseCase(figmaRepository, params.figmaFileHash)
        val imagesMeta = loadImagesPath.execute(null)

        val nodes: Map<String, Node> =
            figmaRepository.getNodes(params.figmaFileHash, imagesMeta.keys.toList()).nodes

        val images = imagesMeta.values.toList()*/
        
        val createFileUseCase = CreateFileUseCase()
        try {
            components.map { component ->
                GlobalScope.launch(Dispatchers.IO) {
                    figmaClient.downloadFile(createFileUseCase.execute(
                        CreateFileUseCase.Params(
                            "src/main/res/drawables/",
                            component.name.replace("/", "").lowercase() + ".png",
                            isDirectory = false
                        )
                    ), component.thumbnailUrl)
                }
            }.joinAll()
        } catch (e: Throwable) {
            println("Failed loading")
        }
    }
    data class Params(val figmaFileHash: String,
                      val resultFile: File,
                      val pageName: String? = null,
                      val nodeId: String? = null )
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