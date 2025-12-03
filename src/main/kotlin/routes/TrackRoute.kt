package com.backend.routes

import com.backend.models.ErrorResponse
import com.backend.models.Track
import com.backend.services.TrackService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.trackRoutes() {
    route("/tracks") {
        post {
            try {
                val dto = call.receive<Track>()
                val created = TrackService.create(dto)
                call.respond(HttpStatusCode.Created, created)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }

        get {
            call.respond(TrackService.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]!!
            val track = TrackService.getById(id)
            if (track != null) {
                call.respond(HttpStatusCode.OK, track)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Track no encontrado"))
            }
        }

        put("/{id}") {
            try {
                val id = call.parameters["id"]!!
                val dto = call.receive<Track>()
                if (TrackService.update(id, dto)) {
                    call.respond(HttpStatusCode.OK, dto.copy(id = id))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Track no encontrado"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }

        delete("/{id}") {
            try {
                val id = call.parameters["id"]!!
                if (TrackService.delete(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Track no encontrado"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }
    }
}