package domain_layer.usecases.android

import domain_layer.usecases.LoadTypographyUseCase
import domain_layer.usecases.MobileUtilUseCase
import network_layer.repositories.FigmaRepository

class LoadAndGenerateComposeStyles(private val figmaRepository: FigmaRepository
) : MobileUtilUseCase<LoadAndGenerateComposeStyles.Params, Unit> {
    override fun execute(params: Params) {
        val loadTypographyUseCase = LoadTypographyUseCase(figmaRepository, params.fileHash)
        val styles = loadTypographyUseCase.execute(null)

        val generateComposeTypographyUseCase = GenerateComposeTypographyUseCase()
        generateComposeTypographyUseCase.execute(GenerateComposeTypographyUseCase.Params(styles = styles))
    }

    data class Params(val fileHash: String)
}