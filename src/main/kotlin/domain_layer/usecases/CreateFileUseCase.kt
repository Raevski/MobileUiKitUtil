package domain_layer.usecases

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

class CreateFileUseCase: MobileUtilUseCase<CreateFileUseCase.Params, File> {
    data class Params(val path: String,
                      val fileName: String,
                      val isDirectory: Boolean = true)

    override suspend fun execute(params: Params): File {
        val fullPath = "${params.path}${params.fileName}"

        println("File path is $fullPath")

        if (!params.isDirectory) {
            val file = File(fullPath)

            if (!file.isFile) {
                Path(params.path).createDirectories()
                File(fullPath).createNewFile()
            }

            return File(fullPath)
        }

        val directory = File(fullPath)

        return if (directory.isDirectory) {
            directory
        } else {
            Path(fullPath).createDirectories()
            File(fullPath)
        }
    }
}