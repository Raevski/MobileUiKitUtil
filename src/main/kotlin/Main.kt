import domain_layer.models.UtilParams
import kotlinx.coroutines.runBlocking
import network_layer.FigmaClient
import network_layer.repositories.FigmaRepository

fun main(args: Array<String>) {
    println("Start of figma mobile util for developers")
    println("Program arguments: ${args.joinToString()}")

    val params = UtilParams.createParamsFromArgs(args)

    val figmaClient = FigmaClient(figmaToken = params.figmaToken)

    val repository = FigmaRepository(figmaClient, params.isLogging)

    runBlocking {
        val body = repository.getStyles(params.fileHash)

        val nodeIds = body.meta.styles.map { it.nodeId }

        val nodesResponse = repository.getNodes(params.fileHash, nodeIds)
    }

    figmaClient.clean()
}