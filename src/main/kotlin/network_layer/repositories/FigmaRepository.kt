package network_layer.repositories

import io.ktor.client.call.*
import io.ktor.client.statement.*
import network_layer.FigmaClient
import network_layer.models.styles.FigmaStylesResponse

class FigmaRepository(private val figmaClient: FigmaClient,
                      private val isLogging: Boolean = false) {
    suspend fun getComponents(fileId: String): String {
        val response = figmaClient.getComponents(fileId)

        if (isLogging) {
            println(response.bodyAsText())
        }

        return response.bodyAsText()
    }

    suspend fun getNodes(fileId: String, ids: List<String>): String {
        val response = figmaClient.getNodes(fileId, ids)

        if (isLogging) {
            println(response.bodyAsText())
        }

        return response.bodyAsText()
    }

    suspend fun getStyles(fileId: String): FigmaStylesResponse {
        val response = figmaClient.getStyles(fileId)

        if (isLogging) {
            println(response.bodyAsText())
        }

        return response.body()
    }
}