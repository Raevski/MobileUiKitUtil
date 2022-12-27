package network_layer.repositories

import io.ktor.client.statement.*
import network_layer.FigmaClient

class FigmaRepository(private val figmaClient: FigmaClient) {
    suspend fun getComponents(fileId: String): String {
        val response = figmaClient.getComponents(fileId)

        return response.bodyAsText()
    }

    suspend fun getNodes(fileId: String): String {
        val response = figmaClient.getNodes(fileId)

        return response.bodyAsText()
    }

    suspend fun getStyles(fileId: String): String {
        val response = figmaClient.getStyles(fileId)

        return response.bodyAsText()
    }
}