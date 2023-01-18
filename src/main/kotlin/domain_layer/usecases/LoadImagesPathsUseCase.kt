package domain_layer.usecases

import domain_layer.models.TextStyle
import kotlinx.coroutines.runBlocking
import network_layer.repositories.FigmaRepository

class LoadImagesPathsUseCase (private val figmaRepository: FigmaRepository,
                              private val fileId: String
) : MobileUtilUseCase<LoadImagesPathsUseCase.Params, Map<String, String>>{
    companion object {
        const val NODE_PARAM_SIZE = 500
    }
    override suspend fun execute(params: Params): Map<String, String> {
        val chunksNumber = params.nodeIds.size / NODE_PARAM_SIZE

        val result = mutableMapOf<String, String>()

        var pageStart = 0
        var pageEnd = 0
        for (i in 0 until chunksNumber) {
            pageStart = i * NODE_PARAM_SIZE
            pageEnd = i * NODE_PARAM_SIZE + NODE_PARAM_SIZE
            if (pageEnd > params.nodeIds.size) {
                pageEnd = params.nodeIds.size - 1
            }
            result.putAll(figmaRepository.getImages(fileId,
                params.nodeIds.slice(pageStart..pageEnd),
                params.format).images)
        }
        if (result.size < params.nodeIds.size) {
            result.putAll(figmaRepository.getImages(fileId,
                params.nodeIds.slice(pageEnd until params.nodeIds.size),
                params.format).images)
        }

        return result
    }

    data class Params(val format: String, val nodeIds: List<String> = listOf())
}