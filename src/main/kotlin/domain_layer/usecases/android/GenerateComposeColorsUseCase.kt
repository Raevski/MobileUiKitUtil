package domain_layer.usecases.android

import domain_layer.models.Color
import domain_layer.usecases.MobileUtilUseCase
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class GenerateComposeColorsUseCase: MobileUtilUseCase<GenerateComposeColorsUseCase.Params, Unit> {

    companion object {
        const val COMPOSE_STYLE_CLASS_PACKAGE_NAME = "androidx.compose.ui.text"
        const val ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME = "androidx.compose.runtime"

    }

    data class Params(val packageName: String = "com.example.hello",
                      val className: String = "Сolors",
                      val colors: List<Color> = listOf(),
                      val file: File)

    override fun execute(params: Params) {
        createXmlForColors(params.colors)
    }

    private fun createXmlForColors(colors: List<Color>) {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.newDocument()
        val rootElement = document.createElement("resources")
        document.appendChild(rootElement)

        colors.forEach { color ->
            val em = document.createElement("color")
            em.setAttribute("name", color.name)
            em.appendChild(document.createTextNode("#000000"))
            rootElement.appendChild(em)
        }

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")

        val source = DOMSource(document)
        val result = StreamResult("colors.xml")
        transformer.transform(source, result)
    }
}