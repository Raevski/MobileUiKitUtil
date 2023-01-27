package network_layer

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import java.io.File

class FigmaClient(private val baseUrl: String = NetworkConsts.FIGMA_API_URL,
                  private val figmaToken: String = "") {

    private val fileDownloaderClient = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 13000
        }
    }

    private val client: HttpClient = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            filter { request ->
                request.url.host.contains(baseUrl)
            }
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        expectSuccess = true
    }

    private suspend fun makeRequest(pathSegments: List<String>,
                                    params: Map<String, List<String>> = hashMapOf()
    ): HttpResponse {
        val response: HttpResponse = client.get(baseUrl) {
            url {
                headers {
                    append("X-Figma-Token", figmaToken)
                }
                appendEncodedPathSegments(pathSegments)
                params.forEach {
                    parameters.append(it.key, it.value.joinToString(separator = ","))
                }
            }
        }

        println(response.request)

        return response
    }

    suspend fun getNodes(fileId: String, ids: List<String>): HttpResponse {
        return makeRequest(listOf("files", fileId, "nodes"), mapOf("ids" to ids))
    }

    suspend fun getStyles(fileId: String): HttpResponse {
        return makeRequest(listOf("files", fileId, "styles"))
    }

    suspend fun getImages(fileId: String,
                          format: String,
                          ids: List<String>,
                          scale: String = ""): HttpResponse {
        val paramsMap = mutableMapOf("ids" to ids,
            "format" to listOf(format))

        if (scale.isNotEmpty()) {
            paramsMap["scale"] = listOf(scale)
        }

        return makeRequest(listOf("images", fileId), paramsMap)

    }

    suspend fun getComponents(fileId: String): HttpResponse {
        return makeRequest(listOf("files", fileId, "components"))
    }

    @OptIn(InternalAPI::class)
    suspend fun downloadFile(file: File, url: String) {
        val call = fileDownloaderClient.get {
            url(url)
            method = HttpMethod.Get
        }
        if (!call.status.isSuccess()) {
            println("Failed while loading ")
            return
        }
        call.content.copyAndClose(file.writeChannel())
        println("Successfully download of icon $url")
    }

    fun clean() {
        client.close()
        fileDownloaderClient.close()
    }
}