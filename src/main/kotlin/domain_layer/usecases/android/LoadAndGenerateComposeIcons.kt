package domain_layer.usecases.android

import domain_layer.models.ImageToDownload
import domain_layer.usecases.CreateFileUseCase
import domain_layer.usecases.LoadComponents
import domain_layer.usecases.LoadImagesPathsUseCase
import domain_layer.usecases.MobileUtilUseCase
import kotlinx.coroutines.*
import network_layer.FigmaClient
import network_layer.models.nodes.Node
import network_layer.repositories.FigmaRepository
import svg_to_vector_drawable_converter.Svg2Vector
import java.io.File
import java.io.FileOutputStream
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

        val componentsNodes = components.map { it.nodeId }

        val loadImagesPath = LoadImagesPathsUseCase(figmaRepository, params.figmaFileHash)
        val imagesMeta = loadImagesPath.execute(LoadImagesPathsUseCase.Params("svg", componentsNodes))

        val createFileUseCase = CreateFileUseCase()
        val imagesToDownload = components.map { component ->
            ImageToDownload(component.nodeId, imagesMeta[component.nodeId].orEmpty(),
                component.svgName,
                component.clearedName)
        }

        try {
            imagesToDownload.map { image ->
                GlobalScope.launch(Dispatchers.IO) {
                    val svgFile = createFileUseCase.execute(
                        CreateFileUseCase.Params(
                            "src/main/res/drawables/",
                            image.imageFileName,
                            isDirectory = false
                        )
                    )
                    figmaClient.downloadFile(svgFile, image.path)
                    Svg2Vector.parseSvgToXml(svgFile,
                        FileOutputStream("src/main/res/drawables/${image.assetName}.xml"))
                    //svgFile.delete()
                }
            }.joinAll()
        } catch (e: Throwable) {
            println("Failed loading")
        }

        val generateComposeIcons = GenerateComposeIcons()
        generateComposeIcons.execute(GenerateComposeIcons.Params(imageNames = imagesToDownload.map {it.assetName},
            file = params.resultFile))
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