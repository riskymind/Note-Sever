package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        // localhost:8080/home
        get("/") {
            call.respondText("Hello World!")
        }
        // localhost:8080/note/id
        get("/note/{id}") {
            val id = call.parameters["id"] ?: return@get
            call.respond(id)
        }

        // localhost:8080/notes?q=value
        get("/note") {
            val id = call.request.queryParameters["id"] ?: return@get
            call.respond(id)
        }

        route("/notes") {
            route("/create") {
                // localHost:8080/notes/create
                post {
                    val body = call.receive<String>()
                    call.respond(body)
                }
            }
            // localHost:8080/notes/
            delete {
                val body = call.receive<String>()
                call.respond(body)
            }
        }


    }
}
