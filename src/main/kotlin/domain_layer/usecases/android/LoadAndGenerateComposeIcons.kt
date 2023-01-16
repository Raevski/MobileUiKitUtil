package domain_layer.usecases.android

import domain_layer.usecases.CreateFileUseCase
import domain_layer.usecases.LoadImagesPathsUseCase
import domain_layer.usecases.MobileUtilUseCase
import kotlinx.coroutines.*
import network_layer.FigmaClient
import network_layer.repositories.FigmaRepository
import java.io.File
import java.net.URI

class LoadAndGenerateComposeIcons(
    private val figmaRepository: FigmaRepository,
    private val figmaClient: FigmaClient
): MobileUtilUseCase<LoadAndGenerateComposeIcons.Params, Unit> {
    override fun execute(params: Params) {
        val loadImagesPath = LoadImagesPathsUseCase(figmaRepository, params.figmaFileHash)
        val images = loadImagesPath.execute(null)

        val createFileUseCase = CreateFileUseCase()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                images.map { imagePath ->
                    val file = createFileUseCase.execute(
                        CreateFileUseCase.Params("src/main/res/drawables/", imagePath.getFileNameFromUrl())
                    )
                    launch { figmaClient.downloadFile(file, imagePath) }
                }.joinAll()
            } catch (e: Throwable) {
                println("Failed loading ")
            }
        }
    }

    data class Params(val figmaFileHash: String, val resultFile: File)
}

fun String.getFileNameFromUrl(): String {
    val uri = URI(this)
    val segments = uri.path.split("/")
    return segments[segments.size - 1]
}