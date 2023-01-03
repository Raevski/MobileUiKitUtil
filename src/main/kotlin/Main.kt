import domain_layer.usecases.CreateFileUseCase
import domain_layer.usecases.ParseConfigUseCase
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

    val params = ParseConfigUseCase().execute(ParseConfigUseCase.Params(args))

    val figmaClient = FigmaClient(figmaToken = params.figmaToken)
    val repository = FigmaRepository(figmaClient, params.isLogging)

    val createFileUseCase = CreateFileUseCase()
    val file = createFileUseCase.execute(
        CreateFileUseCase.Params(
            params.resultPath,
            "${params.resourceType.resourceTypeString}.kt"
        )
    )

    val loadAndGenerateComposeStyles = LoadAndGenerateComposeStyles(repository)
    loadAndGenerateComposeStyles.execute(LoadAndGenerateComposeStyles.Params(params.fileHash, file))

    figmaClient.clean()

    println("Stop of figma mobile util for developers")
}
