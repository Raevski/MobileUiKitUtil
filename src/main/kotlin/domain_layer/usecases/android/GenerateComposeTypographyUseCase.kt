package domain_layer.usecases.android

import com.squareup.kotlinpoet.*
import domain_layer.models.TextStyle
import domain_layer.usecases.MobileUtilUseCase

class GenerateComposeTypographyUseCase : MobileUtilUseCase<GenerateComposeTypographyUseCase.Params, Unit> {
    companion object {
        const val COMPOSE_STYLE_CLASS_PACKAGE_NAME = "androidx.compose.ui.text"
        const val ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME = "androidx.compose.runtime"

    }
    override fun execute(params: Params) {
        val immutableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME, "Immutable")

        val file = FileSpec.builder("params.packageName", params.className)
            .addType(
                addPropertiesForStyles(
                    TypeSpec.classBuilder(params.className)
                        .addAnnotation(immutableAnnotationClass), params.styles).build()

            )
            .build()

        file.writeTo(System.out)
    }

    private fun addPropertiesForStyles(
        classBuilder: TypeSpec.Builder,
        styles: List<TextStyle>
    ): TypeSpec.Builder {

        val composeTextStyleClass = ClassName(COMPOSE_STYLE_CLASS_PACKAGE_NAME, "TextStyle")
        val composableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME, "Composable")

        var augmentedClassBuilder = classBuilder

        styles.forEach { style ->
            augmentedClassBuilder = classBuilder.addProperty(
                PropertySpec.builder(
                    style.name,
                    WildcardTypeName.producerOf(composeTextStyleClass)
                )
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return %S", "foo")
                            .addAnnotation(composableAnnotationClass)
                            .build()
                    )
                .build()
            )
        }

        return augmentedClassBuilder
    }

    data class Params(val packageName: String = "com.example.hello",
                      val className: String = "Typography",
                      val styles: List<TextStyle> = listOf()
    )

}