package domain_layer.usecases

import domain_layer.models.TextStyle
import kotlinx.coroutines.runBlocking
import network_layer.repositories.FigmaRepository

class LoadImagesPathsUseCase (private val figmaRepository: FigmaRepository,
                              private val fileId: String
) : MobileUtilUseCase<Nothing?, List<String>>{
    override fun execute(params: Nothing?): List<String> {
        val imagesPathsInfo: List<String>
        runBlocking {
            imagesPathsInfo = figmaRepository.getImages(fileId).images.values.toList()
        }

        return imagesPathsInfo
    }
}