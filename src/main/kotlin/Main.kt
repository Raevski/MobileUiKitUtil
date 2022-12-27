import io.ktor.client.engine.cio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import network_layer.FigmaClient
import network_layer.repositories.FigmaRepository

fun main(args: Array<String>) {
    println("Start of figma mobile util for developers")

    val repository = FigmaRepository(FigmaClient())
    GlobalScope.launch(Dispatchers.IO) {
        repository.getKtorResponse()
    }

    println("Program arguments: ${args.joinToString()}")
}