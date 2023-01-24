package domain_layer.usecases

import config_dsl.MobileFigmaExportConfig
import config_dsl.Utils
import domain_layer.models.UtilParams
import java.io.File
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ResultWithDiagnostics

class ParseConfigUseCase : MobileUtilUseCase<ParseConfigUseCase.Params, UtilParams> {
    class Params(val args: Array<String>)

    override suspend fun execute(params: Params): UtilParams {
        val scriptFile = File(params.args[0])

        println("Executing script $scriptFile")

        val configParsingResult = Utils.evalFile(scriptFile) as ResultWithDiagnostics.Success
        val config = (configParsingResult.value.returnValue as ResultValue.Value).value as MobileFigmaExportConfig

        if (config.isLogging) {
            println(config)
        }

        println("Config is successfully parsed")

        return UtilParams(config.targetPlatform,
            config.targetResourceType,
            config.figmaToken,
            config.fileHash,
            config.isLogging,
            config.resultPath,
            config.nodeId,
            config.pageName,
            config.showkaseEnabled)
    }
}