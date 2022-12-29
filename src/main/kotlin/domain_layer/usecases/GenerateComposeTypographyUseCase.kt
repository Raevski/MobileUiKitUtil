package domain_layer.usecases

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member

class GenerateComposeTypographyUseCase : MobileUtilUseCase<GenerateComposeTypographyUseCase.Params, Unit> {
    companion object {
        const val COMPOSE_STYLE_CLASS_PACKAGE_NAME = "androidx.compose.ui.text"
        const val IMMUTABLE_ANNOTATION_CLASS_PACKAGE_NAME = "androidx.compose.runtime"

    }
    override fun execute(params: Params) {
        val composeTextStyleClass = ClassName(COMPOSE_STYLE_CLASS_PACKAGE_NAME, "TextStyle")
        val immutableAnnotationClass = ClassName(IMMUTABLE_ANNOTATION_CLASS_PACKAGE_NAME, "Immutable")

        val file = FileSpec.builder("params.packageName", params.className)
            .addType(
                TypeSpec.classBuilder(params.className)
                    .addProperty(
                        PropertySpec.builder("testStyle",
                            WildcardTypeName.producerOf(composeTextStyleClass),
                            listOf())
                            .build()
                    )
                    .addAnnotation(immutableAnnotationClass)
                    .build()
            )
            .build()

        file.writeTo(System.out)
    }

    data class Params(val packageName: String = "com.example.hello",
                      val className: String = "Typography")

}