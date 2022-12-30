import domain_layer.models.UtilParams
import domain_layer.usecases.android.GenerateComposeTypographyUseCase
import domain_layer.usecases.LoadTypographyUseCase
import domain_layer.usecases.android.LoadAndGenerateComposeStyles
import network_layer.FigmaClient
import network_layer.repositories.FigmaRepository

fun main(args: Array<String>) {
    println("Start of figma mobile util for developers")
    println("Program arguments: ${args.joinToString()}")

    val params = UtilParams.createParamsFromArgs(args)

    val figmaClient = FigmaClient(figmaToken = params.figmaToken)
    val repository = FigmaRepository(figmaClient, params.isLogging)

    val loadAndGenerateComposeStyles = LoadAndGenerateComposeStyles(repository)
    loadAndGenerateComposeStyles.execute(LoadAndGenerateComposeStyles.Params(params.fileHash))

    figmaClient.clean()
}