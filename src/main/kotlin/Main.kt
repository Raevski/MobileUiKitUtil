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

    val figmaClient = FigmaClient(figmaToken = args[0])

    val repository = FigmaRepository(figmaClient)

    runBlocking {
        repository.getNodes(args[1])
    }

    figmaClient.clean()
}