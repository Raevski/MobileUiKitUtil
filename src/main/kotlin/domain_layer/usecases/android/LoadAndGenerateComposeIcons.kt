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
    private val createFileUseCase = CreateFileUseCase()

    companion object {
        val scalesWithDirectories = mapOf(
            "1" to "drawable-mdpi",
            "1.5" to "drawable-hdpi",
            "2" to "drawable-xhdpi",
            "3" to "drawable-xxhdpi",
            "4" to "drawable-xxxhdpi")
    }
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

        val imagesToDownload = components.map { component ->
            ImageToDownload(component.nodeId, imagesMeta[component.nodeId].orEmpty(),
                component.svgName,
                component.clearedName)
        }

        try {
            downloadFiles(imagesToDownload,
                params.resourcesPath,
                params.figmaFileHash,
                componentsNodes)
        } catch (e: Throwable) {
            println("Failed loading")
        }

        val generateComposeIcons = GenerateComposeIcons()
        generateComposeIcons.execute(GenerateComposeIcons.Params(imageNames = imagesToDownload,
            file = params.resultFile,
            showkaseEnabled = params.showkaseEnabled,
            resultClassName = params.resultClassName,
            packageName = params.resultPackageName))
    }

    private suspend fun downloadFiles(
        imagesToDownload: List<ImageToDownload>,
        resourcesPath: String,
        figmaFileHash: String,
        componentsNodes: List<String>
    ) {
        imagesToDownload.map { image ->
            GlobalScope.launch(Dispatchers.IO) {
                val svgFile = createFileUseCase.execute(
                    CreateFileUseCase.Params(
                        "$resourcesPath/drawable/",
                        image.imageFileName,
                        isDirectory = false
                    )
                )
                figmaClient.downloadFile(svgFile, image.path)
                val errors = Svg2Vector.parseSvgToXml(svgFile,
                    FileOutputStream("${resourcesPath}/drawable/${image.assetName}.xml"))
                if (errors.isNullOrEmpty()) {
                    File("${resourcesPath}${image.imageFileName}").delete()
                } else {
                    File("${resourcesPath}${image.assetName}.xml").delete()
                    downloadPngFiles(figmaFileHash, componentsNodes, image, resourcesPath)
                }
            }
        }.joinAll()
    }

    private suspend fun downloadPngFiles(
        figmaFileHash: String,
        componentsNodes: List<String>,
        image: ImageToDownload,
        resourcesPath: String) {
        scalesWithDirectories.forEach {
            val loadImagesPath = LoadImagesPathsUseCase(figmaRepository, figmaFileHash)
            val nodeIdWithImagePath = loadImagesPath.execute(
                LoadImagesPathsUseCase.Params("png", componentsNodes.filter { it == image.nodeId } ,it.key))
            figmaClient.downloadFile(createFileUseCase.execute(CreateFileUseCase.Params(
                "${resourcesPath}/${it.value}/",
                image.assetName + ".png",
                isDirectory = false
            )), nodeIdWithImagePath.values.firstOrNull().orEmpty())
        }
    }

    data class Params(val figmaFileHash: String,
                      val resultFile: File,
                      val pageName: String? = null,
                      val nodeId: String? = null,
                      val resourcesPath: String = "src/main/res/drawables/",
                      val showkaseEnabled: Boolean = false,
                      val resultClassName: String = "Icons",
                      val resultPackageName: String = "com.example.hello")
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