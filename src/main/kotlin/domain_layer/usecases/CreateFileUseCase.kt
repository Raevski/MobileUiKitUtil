package domain_layer.usecases

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class CreateFileUseCase: MobileUtilUseCase<CreateFileUseCase.Params, File> {
    data class Params(val path: String,
                      val fileName: String,
                      val fromScratch: Boolean = true)

    override fun execute(params: Params): File {
        val fileName = "./${params.path}/}"

        println("File path is ${fileName}")

        return Files.createDirectory(Paths.get(fileName)).toFile()
    }
}