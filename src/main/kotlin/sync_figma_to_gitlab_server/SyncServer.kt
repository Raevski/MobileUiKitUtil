package sync_figma_to_gitlab_server

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.gzip
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


class SyncServer {
    companion object {
        private const val PORT = 3000
    }
    fun start () {
        embeddedServer(Netty, PORT, watchPaths = emptyList()) {
            // provides the automatic content conversion of requests based on theirContent-Type
            // and Accept headers. Together with the json() setting, this enables automatic
            // serialization and deserialization to and from JSON – allowing
            // us to delegate this tedious task to the framework.
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
            // configures Cross-Origin Resource Sharing. CORS is needed to make calls from arbitrary
            // JavaScript clients, and helps us prevent issues down the line.
            install(CORS) {
                method(HttpMethod.Get)
                method(HttpMethod.Post)
                method(HttpMethod.Delete)
                anyHost()
            }
            // Greatly reduces the amount of data that's needed to be sent to the client by
            // gzipping outgoing content when applicable.
            install(Compression) {
                gzip()
            }
            routing {
                get("/") {
                    call.respondText(
                        text = "Hello!! You are here in Figma sync server",
                        contentType = ContentType.Text.Plain
                    )
                }
                get("/figmaCallback") {
                    call.respond(HttpStatusCode.OK, BaseResponse("FIGMA CALLBACK"))
                }
                get("/figmaCallback/{id}") {
                    val id = call.parameters["id"]
                    call.respond(HttpStatusCode.OK, BaseResponse("CALLBACK ${id}"))
                }
            }
        }.start()
    }
}

fun main(args: Array<String>) {
    SyncServer().start()
}
data class BaseResponse<T>(val data: T? = null, val error: String? = null)