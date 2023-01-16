package domain_layer.usecases.android

import domain_layer.usecases.LoadColorsUseCase
import domain_layer.usecases.MobileUtilUseCase
import network_layer.repositories.FigmaRepository
import java.io.File

class LoadAndGenerateComposeColors(private val figmaRepository: FigmaRepository
) : MobileUtilUseCase<LoadAndGenerateComposeColors.Params, Unit> {
    override fun execute(params: Params) {
        val loadColorsUseCase = LoadColorsUseCase(figmaRepository, params.figmaFileHash)
        val colors = loadColorsUseCase.execute(null)

        val generateComposeColorsUseCase = GenerateComposeColors()
        generateComposeColorsUseCase.execute(GenerateComposeColors.Params(colors = colors,
            file = params.resultFile))
    }

    data class Params(val figmaFileHash: String, val resultFile: File)
}