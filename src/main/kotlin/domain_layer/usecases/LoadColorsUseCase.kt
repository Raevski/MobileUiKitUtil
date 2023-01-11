package domain_layer.usecases

import domain_layer.models.Color
import domain_layer.models.TextStyle
import network_layer.repositories.FigmaRepository

class LoadColorsUseCase(private val figmaRepository: FigmaRepository,
                        private val fileId: String) : MobileUtilUseCase<Nothing?, List<Color>>{
    override fun execute(params: Nothing?): List<Color> {
        return listOf()
    }
}