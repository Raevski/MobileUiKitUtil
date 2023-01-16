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
        const val COMPOSE_COLOR_CLASS_PACKAGE_NAME = "androidx.compose.ui.text"
        const val ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME = "androidx.compose.runtime"
    }

    data class Params(val packageName: String = "com.example.hello",
                      val className: String = "Сolors",
                      val colors: List<Color> = listOf(),
                      val file: File)

    private val composeTextStyleClass = ClassName(COMPOSE_COLOR_CLASS_PACKAGE_NAME,
        "Color")
    private val composableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
        "Composable")
    private val composableImmutableAnnotationClass = ClassName(ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
        "ReadOnlyComposable")

    override fun execute(params: Params) {
        createXmlForColors(params.colors)
        createComposeClasses(params)
    }

    private fun createComposeClasses(params: Params) {
        val immutableAnnotationClass = ClassName(GenerateComposeTypography.ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME,
            "Immutable")

        val builder = FileSpec.builder(params.packageName, params.className)
            .addType(
                addPropertiesForColors(
                    TypeSpec.objectBuilder(params.className)
                        .addAnnotation(immutableAnnotationClass), params.colors).build()

            )
        
        builder.build().writeTo(params.file)
    }

    private fun addPropertiesForColors(
        classBuilder: TypeSpec.Builder,
        colors: List<Color>
    ): TypeSpec.Builder {

        var augmentedClassBuilder = classBuilder

        colors.forEach { color ->
            augmentedClassBuilder = classBuilder.addProperty(
                PropertySpec.builder(
                    color.nameForCode,
                    composeTextStyleClass
                ).getter(
                    getterBuilder(color).build()
                ).build()
            )
        }

        return augmentedClassBuilder
    }

    private fun getterBuilder(color: Color): FunSpec.Builder {
        return FunSpec.getterBuilder()
            .addCode("colorResource(id = R.color.${color.nameForResource})")
            /*.beginControlFlow("if (isSystemInDarkTheme()) {\n" +
                    "   colorResource(id = R.color.color_light_background_additional_one)\n" +
                    "   } else {\n" +
                    "   colorResource(id = R.color.color_light_background_additional_one)\n" +
                    "   }")*/
            //.endControlFlow()
            .addAnnotation(composableAnnotationClass)
            .addAnnotation(composableImmutableAnnotationClass)
    }

    private fun createXmlForColors(colors: List<Color>, resourceFilePath: String = "src/main/res/exported_colors.xml") {
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