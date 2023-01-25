package domain_layer.usecases.android

import domain_layer.usecases.LoadTypographyUseCase
import domain_layer.usecases.MobileUtilUseCase
import network_layer.repositories.FigmaRepository
import java.io.File

class LoadAndGenerateComposeStyles(private val figmaRepository: FigmaRepository
) : MobileUtilUseCase<LoadAndGenerateComposeStyles.Params, Unit> {
    override suspend fun execute(params: Params) {
        val loadTypographyUseCase = LoadTypographyUseCase(figmaRepository, params.figmaFileHash)
        val styles = loadTypographyUseCase.execute(null)

        val generateComposeTypography = GenerateComposeTypography()
        generateComposeTypography.execute(GenerateComposeTypography.Params(styles = styles,
            file = params.resultFile,
            showkaseEnabled = params.showkaseEnabled,
            resultClassName = params.resultClassName,
            packageName = params.resultPackageName))
    }

    data class Params(val figmaFileHash: String,
                      val resultFile: File,
                      val showkaseEnabled: Boolean = false,
                      val resultClassName: String = "Typography",
                      val resultPackageName: String = "com.example.hello")
}