package domain_layer.usecases

import domain_layer.models.TextStyle
import kotlinx.coroutines.runBlocking
import network_layer.models.nodes.LineHeightUnit
import network_layer.models.nodes.NodesResponse
import network_layer.models.nodes.TextCase
import network_layer.models.nodes.TypeStyle
import network_layer.models.styles.FigmaStylesResponse
import network_layer.repositories.FigmaRepository

class LoadTypographyUseCase(private val figmaRepository: FigmaRepository,
                            private val fileId: String) : MobileUtilUseCase<Nothing?, List<TextStyle>>{
    override suspend fun execute(params: Nothing?): List<TextStyle> {
        println("Start loading typography from file $fileId")


        val stylesResponseBody = figmaRepository.getStyles(fileId)

        val nodeIds = stylesResponseBody.meta.styles.map { it.nodeId }

        val nodesResponseBody = figmaRepository.getNodes(fileId, nodeIds)

        println("Typography was successfully exported from file $fileId")

        val resultStyles = stylesResponseBody.meta.styles.map { style ->
            val node = nodesResponseBody.nodes.get(style.nodeId)
            val textStyle: TypeStyle? = node?.document?.style

            val lineHeight: Double? = if (textStyle?.lineHeightUnit == LineHeightUnit.INTRINSIC) { null } else {
                textStyle?.lineHeightPx
            }

            val textCase = textStyle?.textCase ?: TextCase.ORIGINAL

            TextStyle(
                name = style.name,
                fontName = textStyle?.fontPostScriptName ?: textStyle?.fontFamily ?: "",
                fontSize = textStyle?.fontSize ?: .0,
                lineHeight = lineHeight ?: .0,
                letterSpacing = textStyle?.letterSpacing ?: .0,
                textCase = textCase,
                fontWeight = textStyle?.fontWeight ?: .0
            )
        }

        println("Typography style list is $resultStyles")

        return resultStyles
    }
}