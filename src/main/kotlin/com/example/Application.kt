package com.example

import com.example.auth.JwtService
import com.example.auth.hash
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.repository.DatabaseFactory
import com.example.repository.Repo

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()
        configureRouting()
        configureSecurity()
        configureSerialization()
    }.start(wait = true)
}
