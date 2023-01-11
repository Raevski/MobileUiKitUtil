package domain_layer.usecases

import domain_layer.models.Color
import domain_layer.models.TextStyle
import kotlinx.coroutines.runBlocking
import network_layer.models.nodes.NodesResponse
import network_layer.models.styles.FigmaStyleData
import network_layer.models.styles.FigmaStyleType
import network_layer.models.styles.FigmaStylesResponse
import network_layer.repositories.FigmaRepository

class LoadColorsUseCase(private val figmaRepository: FigmaRepository,
                        private val fileId: String) : MobileUtilUseCase<Nothing?, List<Color>>{
    override fun execute(params: Nothing?): List<Color> {
        println("Start loading colors from file $fileId")

        val nodesResponseBody: NodesResponse
        val stylesResponseBody: FigmaStylesResponse
        val styles: List<FigmaStyleData>

        runBlocking {
            stylesResponseBody = figmaRepository.getStyles(fileId)

            styles = stylesResponseBody.meta.styles.filter {
                it.styleType == FigmaStyleType.FILL && it.useStyle()
            }

            val nodeIds = styles.map { it.nodeId }

            nodesResponseBody = figmaRepository.getNodes(fileId, nodeIds)
        }

        return nodesResponseBody.nodes.keys.map { key ->
            val node = nodesResponseBody.nodes[key]
            val fill = node!!.document.fills.first()
            Color(styles.find { it.nodeId == key }?.name.orEmpty(),
                "",
                fill.color?.r ?: .0,
                fill.color?.g ?: .0,
                fill.color?.b  ?: .0,
                fill.opacity ?: .0)
        }
    }

}