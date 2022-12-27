package network_layer.repositories

import io.ktor.client.statement.*
import network_layer.FigmaClient

class FigmaRepository(private val figmaClient: FigmaClient) {
    suspend fun getKtorResponse() {
        val response = figmaClient.makeRequest("https://ktor.io/")

        println(response.bodyAsText())
    }
}