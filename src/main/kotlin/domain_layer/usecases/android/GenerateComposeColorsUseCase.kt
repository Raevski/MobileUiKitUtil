package domain_layer.usecases.android

import domain_layer.models.TextStyle
import domain_layer.usecases.MobileUtilUseCase
import java.io.File

class GenerateComposeColorsUseCase: MobileUtilUseCase<GenerateComposeColorsUseCase.Params, Unit> {

    companion object {
        const val COMPOSE_STYLE_CLASS_PACKAGE_NAME = "androidx.compose.ui.text"
        const val ANDROIDX_COMPOSE_ANNOTATION_PACKAGE_NAME = "androidx.compose.runtime"

    }

    data class Params(val packageName: String = "com.example.hello",
                      val className: String = "Сolors",
                      val styles: List<TextStyle> = listOf(),
                      val file: File)

    override fun execute(params: Params) {

    }
}