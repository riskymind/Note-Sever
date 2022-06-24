package com.example

import com.example.auth.JwtService
import com.example.auth.hash
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.repository.DatabaseFactory
import com.example.repository.Repo
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt(), host = "0.0.0.0") {
        DatabaseFactory.init()
        install(Locations)

        configureRouting()
        configureSecurity()
        configureSerialization()
    }.start(wait = true)
}
