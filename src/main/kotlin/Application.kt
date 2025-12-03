package com.backend

import com.backend.config.configureDatabases
import com.backend.config.configureRouting
import com.backend.config.configureSerialization
import com.backend.config.configureStatusPages
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 3000, host = "0.0.0.0") {
        configureSerialization()
        configureDatabases()
        configureRouting()
        configureStatusPages()
    }.start(wait = true)
}