package network_layer

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.*

class FigmaClient(private val baseUrl: String = NetworkConsts.FIGMA_API_URL,
                  private val figmaToken: String = "") {
    private val client: HttpClient = HttpClient() {
        //TODO: Should fix error SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".

        /*install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            filter { request ->
                request.url.host.contains("ktor.io")
            }
        }*/
        //expectSuccess = true
    }

    private suspend fun makeRequest(pathSegments: List<String>): HttpResponse {
        val response: HttpResponse = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = baseUrl
                appendEncodedPathSegments(pathSegments)
                headers {
                    append("X-Figma-Token", figmaToken)
                }
            }
        }

        println(response.status)

        return response
    }

    suspend fun getNodes(fileId: String): HttpResponse {
        return makeRequest(listOf("files", fileId, "nodes"))
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