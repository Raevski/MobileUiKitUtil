package domain_layer.usecases

import domain_layer.models.Component
import network_layer.repositories.FigmaRepository

class LoadComponents(private val figmaRepository: FigmaRepository,
                     private val fileId: String
): MobileUtilUseCase<Nothing?, List<Component>> {
    override suspend fun execute(params: Nothing?): List<Component> {
        return figmaRepository.getComponents(fileId).meta.components.map { componentData ->
            Component(key = componentData.key,
                fileKey = componentData.fileKey,
                nodeId = componentData.nodeId,
                thumbnailUrl = componentData.thumbnailUrl,
                name = componentData.name,
                description = componentData.description,
                frameName = componentData.containingFrame.name,
                frameNodeId = componentData.containingFrame.nodeId,
                pageId = componentData.containingFrame.pageId,
                pageName = componentData.containingFrame.pageName)
        }
    }
}