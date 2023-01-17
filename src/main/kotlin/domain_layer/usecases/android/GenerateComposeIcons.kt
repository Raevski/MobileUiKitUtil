package domain_layer.usecases.android

import domain_layer.usecases.MobileUtilUseCase
import java.io.File
import com.squareup.kotlinpoet.*

class GenerateComposeIcons: MobileUtilUseCase<GenerateComposeIcons.Params, Unit> {

    companion object {
        const val COMPOSE_COLOR_CLASS_PACKAGE_NAME = "androidx.compose.material"
        const val ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME = "androidx.compose.runtime"
    }

    data class Params(val packageName: String = "com.example.hello",
                      val className: String = "Icons",
                      val imageNames: List<String> = listOf(),
                      val file: File)

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

        val builder = FileSpec.builder(params.packageName, params.className)
            .addType(
                addPropertiesForIcons(
                    TypeSpec.objectBuilder(params.className)
                        .addAnnotation(immutableAnnotationClass), params.imageNames).build()

            )
        
        builder.build().writeTo(params.file)
    }

    private fun addPropertiesForIcons(
        classBuilder: TypeSpec.Builder,
        colors: List<String>
    ): TypeSpec.Builder {

        var augmentedClassBuilder = classBuilder

        colors.forEach { imageName ->
            augmentedClassBuilder = classBuilder.addProperty(
                PropertySpec.builder(
                    imageName,
                    composeIconClass
                ).getter(
                    getterBuilder(imageName).build()
                ).build()
            )
        }

        return augmentedClassBuilder
    }

    private fun getterBuilder(imageName: String): FunSpec.Builder {
        return FunSpec.getterBuilder()
            .addCode("Icon(painter = painterResource(id = R.drawable.${imageName}))")
            .addAnnotation(composableAnnotationClass)
            .addAnnotation(composableImmutableAnnotationClass)
    }
}