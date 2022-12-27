package network_layer

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class FigmaClient(private val baseUrl: String = NetworkConsts.FIGMA_API_URL,
                  private val figmaToken: String = "") {
    private val client: HttpClient = HttpClient() {
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
            }
            parametersOf(params)
        }

        println(response.request)

        return response
    }

    suspend fun getNodes(fileId: String, ids: List<String>): HttpResponse {
        return makeRequest(listOf("files", fileId, "nodes"), mapOf<String, List<String>>("ids" to ids))
    }

    suspend fun getStyles(fileId: String): HttpResponse {
        return makeRequest(listOf("files", fileId, "styles"))
    }

    suspend fun getComponents(fileId: String): HttpResponse {
        return makeRequest(listOf("files", fileId, "components"))
    }

    fun clean() {
        client.close()
    }
}