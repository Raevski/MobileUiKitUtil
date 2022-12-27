import domain_layer.models.UtilParams
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import network_layer.FigmaClient
import network_layer.repositories.FigmaRepository

fun main(args: Array<String>) {
    println("Start of figma mobile util for developers")
    println("Program arguments: ${args.joinToString()}")

    val params = UtilParams.createParamsFromArgs(args)

    val figmaClient = FigmaClient(figmaToken = params.figmaToken)

    val repository = FigmaRepository(figmaClient)

    runBlocking {
        println(repository.getStyles(params.fileHash))
    }

    figmaClient.clean()
}