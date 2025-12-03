package com.backend.config

import com.backend.routes.albumRoutes
import com.backend.routes.artistaRoutes
import com.backend.routes.trackRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            artistaRoutes()
            albumRoutes()
            trackRoutes()
        }
    }
}