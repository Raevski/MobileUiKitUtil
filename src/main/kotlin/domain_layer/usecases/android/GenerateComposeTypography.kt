package domain_layer.usecases.android

import com.squareup.kotlinpoet.*
import config_dsl.replacedByToken
import domain_layer.models.TextStyle
import domain_layer.usecases.MobileUtilUseCase
import java.io.File

class GenerateComposeTypography : MobileUtilUseCase<GenerateComposeTypography.Params, Unit> {
    companion object {
        const val COMPOSE_STYLE_CLASS_PACKAGE_NAME = "androidx.compose.ui.text"
        const val ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME = "androidx.compose.runtime"
        const val SHOWKASE_PACKAGE_NAME = "com.airbnb.android.showkase.annotation"
    }
    override suspend fun execute(params: Params) {
        val immutableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME, "Immutable")

        val file = FileSpec.builder(params.packageName, params.resultClassName)
            .addType(
                addPropertiesForStyles(
                    TypeSpec.classBuilder(params.resultClassName)
                        .addAnnotation(immutableAnnotationClass),
                    params.styles,
                    params.showkaseEnabled).build()

            )
            .addImport("androidx.compose.ui.unit", "sp")
            .addImport("androidx.compose.ui.text.font", "FontWeight", "FontFamily")
            .build()

        file.writeTo(params.file)
    }

    private fun addPropertiesForStyles(
        classBuilder: TypeSpec.Builder,
        styles: List<TextStyle>,
        showkaseEnabled: Boolean = false
    ): TypeSpec.Builder {

        val composeTextStyleClass = ClassName(COMPOSE_STYLE_CLASS_PACKAGE_NAME, "TextStyle")
        //val composableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME, "Composable")

        var augmentedClassBuilder = classBuilder

        styles.forEach { style ->
            val propertyBuilder = PropertySpec.builder(
                style.name.replacedByToken(),
                composeTextStyleClass
            ).initializer("TextStyle(\n" +
                    "  fontFamily = FontFamily.Default,\n" +
                    "  fontWeight = ${getFontWeightString(style.fontWeight)},\n" +
                    "  fontSize = ${style.fontSize.toInt()}.sp,\n" +
                    "  lineHeight = ${style.lineHeight.toInt()}.sp)")
            if (showkaseEnabled) {
                propertyBuilder.addAnnotation(
                    AnnotationSpec.builder(ClassName(SHOWKASE_PACKAGE_NAME, "ShowkaseTypography"))
                        .addMember("\"${style.name}\"")
                        .addMember("\"uikit_exported\"")
                        .build())
            }
            augmentedClassBuilder = classBuilder.addProperty(
                propertyBuilder
                .build()
            )
        }

        return augmentedClassBuilder
    }

    private fun getFontWeightString(fontWeight: Double) : String {
        return when(fontWeight) {
            700.0 ->
                "FontWeight.Bold"
            600.0 ->
                "FontWeight.SemiBold"
            500.0 ->
                "FontWeight.Medium"
            400.0 ->
                "FontWeight.Normal"
            300.0 ->
                "FontWeight.Light"

            else -> "FontWeight.Normal"
        }
    }

    data class Params(val packageName: String = "com.example.hello",
                      val styles: List<TextStyle> = listOf(),
                      val file: File,
                      val showkaseEnabled: Boolean = false,
                      val resultClassName: String = "Typography",
                      val resourcesResultPath: String = "src/main/res/"
    )

}