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
import java.util.concurrent.atomic.AtomicInteger
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

class LoadAndGenerateComposeIcons(
    private val figmaRepository: FigmaRepository,
    private val figmaClient: FigmaClient
) : MobileUtilUseCase<LoadAndGenerateComposeIcons.Params, Unit> {
    private val createFileUseCase = CreateFileUseCase()

    companion object {
        val scalesWithDirectories = mapOf(
            "1" to "drawable-mdpi",
            "1.5" to "drawable-hdpi",
            "2" to "drawable-xhdpi",
            "3" to "drawable-xxhdpi",
            "4" to "drawable-xxxhdpi"
        )
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
            ImageToDownload(
                component.nodeId, imagesMeta[component.nodeId].orEmpty(),
                component.svgName,
                component.clearedName
            )
        }

        try {
            downloadFiles(
                imagesToDownload,
                params.resourcesPath,
                params.figmaFileHash,
                componentsNodes
            )
        } catch (e: Throwable) {
            println("Failed loading")
        }

        val generateComposeIcons = GenerateComposeIcons()
        generateComposeIcons.execute(
            GenerateComposeIcons.Params(
                imageNames = imagesToDownload,
                file = params.resultFile,
                showkaseEnabled = params.showkaseEnabled,
                resultClassName = params.resultClassName,
                packageName = params.resultPackageName
            )
        )
    }

    private suspend fun downloadFiles(
        imagesToDownload: List<ImageToDownload>,
        resourcesPath: String,
        figmaFileHash: String,
        componentsNodes: List<String>
    ) {
        val failedToConvertToVectorDrawableImages = mutableListOf<ImageToDownload>()
        val filesToDelete = mutableListOf<String>()
        imagesToDownload.map { image ->
            GlobalScope.launch(Dispatchers.IO) {
                val svgFile = createFileUseCase.execute(
                    CreateFileUseCase.Params(
                        "$resourcesPath/drawable/",
                        image.imageFileName,
                        isDirectory = false
                    )
                )
                try {
                    figmaClient.downloadFile(svgFile, image.path)
                } catch (e: Exception) {
                    svgFile.delete()
                }

                val errors = Svg2Vector.parseSvgToXml(
                    svgFile,
                    FileOutputStream("${resourcesPath}/drawable/${image.assetName}.xml")
                )
                if (!errors.isNullOrEmpty()) {
                    filesToDelete.add("${resourcesPath}/drawable/${image.assetName}.xml")
                    failedToConvertToVectorDrawableImages.add(image)
                }
                filesToDelete.add("${resourcesPath}/drawable/${image.imageFileName}")
            }
        }.joinAll()

        downloadRasterFiles(figmaFileHash, failedToConvertToVectorDrawableImages, resourcesPath)

        filesToDelete.forEach {
            Path(it).deleteIfExists()
            println("File $it deleted")
        }
    }

    private suspend fun downloadRasterFiles(
        figmaFileHash: String,
        images: List<ImageToDownload>,
        resourcesPath: String
    ) {
        val loadImagesPath = LoadImagesPathsUseCase(figmaRepository, figmaFileHash)

        var currentCount = AtomicInteger(0)
        val totalCount = images.size * scalesWithDirectories.size
        scalesWithDirectories.map { scaleWithDirectory ->
            GlobalScope.launch(Dispatchers.IO) {
                val nodeIdWithImagePaths = loadImagesPath.execute(
                    LoadImagesPathsUseCase.Params("png", images.map { it.nodeId }, scaleWithDirectory.key)
                )

                nodeIdWithImagePaths.map { nodeWithImagePath ->
                    launch(Dispatchers.IO) {
                        figmaClient.downloadFile(
                            createFileUseCase.execute(
                                CreateFileUseCase.Params(
                                    "${resourcesPath}/${scaleWithDirectory.value}/",
                                    images.find { it.nodeId == nodeWithImagePath.key }!!.assetName + ".png",
                                    isDirectory = false
                                )
                            ), nodeWithImagePath.value
                        )
                        currentCount.getAndAdd(1)
                        println("Downloaded file ${currentCount.get()} from $totalCount total")
                    }
                }.joinAll()
            }
        }.joinAll()
    }

    data class Params(
        val figmaFileHash: String,
        val resultFile: File,
        val pageName: String? = null,
        val nodeId: String? = null,
        val resourcesPath: String = "src/main/res",
        val showkaseEnabled: Boolean = false,
        val resultClassName: String = "Icons",
        val resultPackageName: String = "com.example.hello"
    )
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