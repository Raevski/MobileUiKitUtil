package domain_layer.usecases

import domain_layer.models.TextStyle
import kotlinx.coroutines.runBlocking
import network_layer.models.nodes.NodesResponse
import network_layer.repositories.FigmaRepository

class LoadTypographyUseCase(private val figmaRepository: FigmaRepository,
                            private val fileId: String) {
    fun execute(): List<TextStyle> {
        println("Start loading typography from file $fileId")

        val nodesResponse: NodesResponse
        runBlocking {
            val body = figmaRepository.getStyles(fileId)

            val nodeIds = body.meta.styles.map { it.nodeId }

            nodesResponse = figmaRepository.getNodes(fileId, nodeIds)
        }

        println("Typography was successfully exported from file $fileId")

        return nodesResponse.nodes.map { TextStyle() }
    }
}