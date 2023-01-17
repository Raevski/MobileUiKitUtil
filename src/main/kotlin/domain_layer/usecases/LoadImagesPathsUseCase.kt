package domain_layer.usecases

import domain_layer.models.TextStyle
import kotlinx.coroutines.runBlocking
import network_layer.repositories.FigmaRepository

class LoadImagesPathsUseCase (private val figmaRepository: FigmaRepository,
                              private val fileId: String
) : MobileUtilUseCase<Nothing?, Map<String, String>>{
    override suspend fun execute(params: Nothing?): Map<String, String> {
        return figmaRepository.getImages(fileId).meta.images
    }
}