package domain_layer.usecases.android

import domain_layer.usecases.CreateFileUseCase
import domain_layer.usecases.LoadColorsUseCase
import domain_layer.usecases.LoadImagesPathsUseCase
import domain_layer.usecases.MobileUtilUseCase
import network_layer.FigmaClient
import network_layer.repositories.FigmaRepository
import java.io.File

class LoadAndGenerateComposeIcons(
    private val figmaRepository: FigmaRepository,
    private val figmaClient: FigmaClient
): MobileUtilUseCase<LoadAndGenerateComposeIcons.Params, Unit> {
    override fun execute(params: Params) {
        val loadImagesPath = LoadImagesPathsUseCase(figmaRepository, params.figmaFileHash)
        val images = loadImagesPath.execute(null)

        val createFileUseCase = CreateFileUseCase()

        images.forEach { imagePath ->
            val file =
            figmaClient.downloadFile()
        }
    }

    data class Params(val figmaFileHash: String, val resultFile: File)
}