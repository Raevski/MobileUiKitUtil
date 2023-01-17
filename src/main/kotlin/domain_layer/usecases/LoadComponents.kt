package domain_layer.usecases

import network_layer.repositories.FigmaRepository

class LoadComponents(private val figmaRepository: FigmaRepository,
                     private val fileId: String
): MobileUtilUseCase<Nothing?, String> {
    override suspend fun execute(params: Nothing?): String {
        return figmaRepository.getComponents(fileId).toString()
    }
}