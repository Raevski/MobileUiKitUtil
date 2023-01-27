package domain_layer.usecases.android

import domain_layer.models.Color
import domain_layer.usecases.MobileUtilUseCase
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import com.squareup.kotlinpoet.*

class GenerateComposeColors: MobileUtilUseCase<GenerateComposeColors.Params, Unit> {

    companion object {
        const val COMPOSE_COLOR_CLASS_PACKAGE_NAME = "androidx.compose.ui.graphics"
        const val ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME = "androidx.compose.runtime"
        const val ANDROIDX_COMPOSE_COLOR_RESOURCE = "androidx.compose.ui.res"
        const val SHOWKASE_PACKAGE_NAME = "com.airbnb.android.showkase.annotation"
    }

    data class Params(val packageName: String = "com.example.hello",
                      val colors: List<Color> = listOf(),
                      val file: File,
                      val showkaseEnabled: Boolean = false,
                      val resultClassName: String = "Colors",
                      val resourcesResultPath: String = "src/main/res/")

    private val composeColorClass = ClassName(COMPOSE_COLOR_CLASS_PACKAGE_NAME,
        "Color")
    private val composableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
        "Composable")
    private val composableImmutableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
        "ReadOnlyComposable")

    override suspend fun execute(params: Params) {
        createXmlForColors(params.colors, params.resourcesResultPath + "exported_colors.xml")
        createComposeClasses(params)
    }

    private fun createComposeClasses(params: Params) {
        val immutableAnnotationClass = ClassName(GenerateComposeTypography.ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
            "Immutable")

        val builder = FileSpec.builder(params.packageName, params.resultClassName)
            .addType(
                addPropertiesForColors(
                    TypeSpec.objectBuilder(params.resultClassName)
                        .addAnnotation(immutableAnnotationClass),
                    params.colors,
                    params.showkaseEnabled).build()

            )
        
        builder.build().writeTo(params.file)
    }

    private fun addPropertiesForColors(
        classBuilder: TypeSpec.Builder,
        colors: List<Color>,
        showkaseEnabled: Boolean = false
    ): TypeSpec.Builder {

        var augmentedClassBuilder = classBuilder

        colors.forEach { color ->
            val propertyBuilder = PropertySpec.builder(
                color.nameForCode,
                composeColorClass
            ).initializer(
                initializerBuilder(color)
            )
            if (showkaseEnabled) {
                propertyBuilder.addAnnotation(
                    AnnotationSpec.builder(ClassName(SHOWKASE_PACKAGE_NAME, "ShowkaseColor"))
                        .addMember("\"${color.name}\"")
                        .addMember("\"uikit_exported\"")
                        .build())
            }
            augmentedClassBuilder = classBuilder.addProperty(
                propertyBuilder.build()
            )
        }

        return augmentedClassBuilder
    }

    private fun initializerBuilder(color: Color): String {
        return "Color(red = ${color.redInt}, green = ${color.greenInt}, blue = ${color.blueInt}, alpha = ${color.alphaInt})"
            /*.beginControlFlow("if (isSystemInDarkTheme()) {\n" +
                    "   colorResource(id = R.color.color_light_background_additional_one)\n" +
                    "   } else {\n" +
                    "   colorResource(id = R.color.color_light_background_additional_one)\n" +
                    "   }")*/
            //.endControlFlow()
    }

    private fun createXmlForColors(colors: List<Color>, resourceFilePath: String = "exported_colors.xml") {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.newDocument()
        val rootElement = document.createElement("resources")
        document.appendChild(rootElement)

        colors.forEach { color ->
            val element = document.createElement("color")
            element.setAttribute("name", color.nameForResource)
            element.appendChild(document.createTextNode(color.inHex()))
            rootElement.appendChild(element)
        }

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")

        val source = DOMSource(document)
        val result = StreamResult(resourceFilePath)
        transformer.transform(source, result)
    }
}