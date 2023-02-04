package sync_figma_to_gitlab_server

import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.script
import java.io.File

fun Application.main() {
    val currentDir = File(".").absoluteFile
    environment.log.info("Current directory: $currentDir")

    routing {
        get("/") {
            call.respondHtml {
                body {
                    +"Hello from Sync Util"
                    div {
                        id = "js-response loading..."
                    }
                    script(src = "/static/output.js") {
                    }
                }
            }
        }
        get("/test") {
            call.respond("I am a test response")
        }
        static("/static") {
            resources()
        }
    }
}