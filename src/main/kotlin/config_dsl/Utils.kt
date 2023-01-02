package config_dsl

import java.io.File
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.dependencies.DependsOn
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

object Utils {
    fun evalFile(scriptFile: File): ResultWithDiagnostics<EvaluationResult> {
        val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<ParseConfigScript>()
        return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, null)
    }
}

@KotlinScript(
    // File extension for the script type
    fileExtension = ".muc",
    // Compilation configuration for the script type
    compilationConfiguration = ParseConfigScriptConfiguration::class
)
abstract class ParseConfigScript

object ParseConfigScriptConfiguration: ScriptCompilationConfiguration(
    {
        defaultImports(DependsOn::class, MobileFigmaExportConfig::class)
        defaultImports(DependsOn::class, Tokenization::class)
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
    }
)
