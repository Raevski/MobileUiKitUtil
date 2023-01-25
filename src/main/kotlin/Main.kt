import domain_layer.models.ExportResourceType
import domain_layer.usecases.CreateFileUseCase
import domain_layer.usecases.LoadColorsUseCase
import domain_layer.usecases.ParseConfigUseCase
import domain_layer.usecases.android.LoadAndGenerateComposeColors
import domain_layer.usecases.android.LoadAndGenerateComposeIcons
import domain_layer.usecases.android.LoadAndGenerateComposeStyles
import network_layer.FigmaClient
import network_layer.repositories.FigmaRepository

fun main(args: Array<String>) {
    println("Start of figma mobile util for developers")

    if (args.isEmpty()) {
        println("Please provide config file .muc")
        println("Stop of figma mobile util because of empty arguments")
        return
    }

    println("Program arguments: ${args.joinToString()}")

    val params = ParseConfigUseCase().executeBlocking(ParseConfigUseCase.Params(args))

    val figmaClient = FigmaClient(figmaToken = params.figmaToken)
    val repository = FigmaRepository(figmaClient, params.isLogging)

    val createFileUseCase = CreateFileUseCase()
    val file = createFileUseCase.executeBlocking(
        CreateFileUseCase.Params(
            params.resultPath,
            "${params.resourceType.resourceTypeString}.kt"
        )
    )

    when(params.resourceType) {
        ExportResourceType.TYPOGRAPHY -> {
            val loadAndGenerateComposeStyles = LoadAndGenerateComposeStyles(repository)
            loadAndGenerateComposeStyles.executeBlocking(
                LoadAndGenerateComposeStyles.Params(params.fileHash,
                    file,
                    showkaseEnabled = params.showkaseEnabled,
                    params.resultClassName,
                    resultPackageName = params.resultPackageName))
        }

        ExportResourceType.COLORS -> {
            val loadAndGenerateComposeColors = LoadAndGenerateComposeColors(repository)
            loadAndGenerateComposeColors.executeBlocking(
                LoadAndGenerateComposeColors.Params(params.fileHash,
                    file,
                    params.showkaseEnabled,
                    params.resultClassName,
                    resultPackageName = params.resultPackageName))
        }

        ExportResourceType.ICONS -> {
            val loadAndGenerateComposeIcons = LoadAndGenerateComposeIcons(repository, figmaClient)
            loadAndGenerateComposeIcons.executeBlocking(
                LoadAndGenerateComposeIcons.Params(params.fileHash,
                    file,
                    showkaseEnabled = params.showkaseEnabled,
                    resultClassName = params.resultClassName,
                    resultPackageName = params.resultPackageName))
        }
    }

    figmaClient.clean()

    println("Stop of figma mobile util for developers")
}
