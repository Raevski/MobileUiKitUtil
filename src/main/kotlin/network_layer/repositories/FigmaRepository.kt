package network_layer.repositories

import io.ktor.client.call.*
import io.ktor.client.statement.*
import network_layer.FigmaClient
import network_layer.models.styles.FigmaStylesResponse

class FigmaRepository(private val figmaClient: FigmaClient) {
    suspend fun getComponents(fileId: String): String {
        val response = figmaClient.getComponents(fileId)

        return response.bodyAsText()
    }

    suspend fun getNodes(fileId: String): String {
        val response = figmaClient.getNodes(fileId)

        return response.bodyAsText()
    }

    suspend fun getStyles(fileId: String): FigmaStylesResponse {
        val response = figmaClient.getStyles(fileId)
        println(response.bodyAsText())
        return response.body()
    }
}