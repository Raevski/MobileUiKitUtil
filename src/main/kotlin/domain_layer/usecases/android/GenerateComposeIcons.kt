package domain_layer.usecases.android

import domain_layer.usecases.MobileUtilUseCase
import java.io.File
import com.squareup.kotlinpoet.*
import domain_layer.models.ImageToDownload

class GenerateComposeIcons: MobileUtilUseCase<GenerateComposeIcons.Params, Unit> {

    companion object {
        const val COMPOSE_COLOR_CLASS_PACKAGE_NAME = "androidx.compose.material"
        const val ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME = "androidx.compose.runtime"
    }

    data class Params(val packageName: String = "com.example.hello",
                      val resultClassName: String = "Icons",
                      val imageNames: List<ImageToDownload> = listOf(),
                      val file: File,
                      val showkaseEnabled: Boolean = false,
                      val resourcesResultPath: String = "src/main/res/")

    private val composeIconClass = ClassName(COMPOSE_COLOR_CLASS_PACKAGE_NAME,
        "Icon")
    private val composableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
        "Composable")
    private val composableImmutableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
        "ReadOnlyComposable")

    override suspend fun execute(params: Params) {
        createComposeClasses(params)
    }

    private fun createComposeClasses(params: Params) {
        val immutableAnnotationClass = ClassName(GenerateComposeTypography.ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
            "Immutable")

        val builder = FileSpec.builder(params.packageName, params.resultClassName)
            .addType(
                addPropertiesForIcons(
                    TypeSpec.objectBuilder(params.resultClassName)
                        .addAnnotation(immutableAnnotationClass),
                    params.imageNames,
                    params.showkaseEnabled).build()

            )
        
        builder.build().writeTo(params.file)
    }

    private fun addPropertiesForIcons(
        classBuilder: TypeSpec.Builder,
        images: List<ImageToDownload>,
        showkaseEnabled: Boolean = false
    ): TypeSpec.Builder {

        var augmentedClassBuilder = classBuilder

        images.forEach { image ->
            val propertyBuilder = PropertySpec.builder(
                image.composeIconName(),
                composeIconClass
            ).initializer(
                initializerBuilder(image.assetName)
            )

            /*if (showkaseEnabled) {
                propertyBuilder.addAnnotation(
                    AnnotationSpec.builder(ClassName(GenerateComposeColors.SHOWKASE_PACKAGE_NAME, "ShowkaseIcon"))
                        .addMember("\"${image.assetName}\"")
                        .addMember("\"uikit_exported\"")
                        .build())
            }*/
            augmentedClassBuilder = classBuilder.addProperty(
                propertyBuilder.build()
            )
        }

        return augmentedClassBuilder
    }

    private fun initializerBuilder(imageName: String): String {
        return "Icon(painter = painterResource(id = R.drawable.${imageName}))"
    }
}