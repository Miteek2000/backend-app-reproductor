package com.backend.routes

import com.backend.models.Artista
import com.backend.models.ErrorResponse
import com.backend.services.ArtistaService
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.artistaRoutes() {
    route("/artistas") {
        post {
            try {
                val dto = call.receive<Artista>()
                val created = ArtistaService.create(dto)
                call.respond(HttpStatusCode.Created, created)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }

        get {
            call.respond(ArtistaService.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]!!
            val artista = ArtistaService.getById(id)
            if (artista != null) {
                call.respond(HttpStatusCode.OK, artista)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Artista no encontrado"))
            }
        }

        put("/{id}") {
            try {
                val id = call.parameters["id"]!!
                val dto = call.receive<Artista>()
                if (ArtistaService.update(id, dto)) {
                    call.respond(HttpStatusCode.OK, dto.copy(id = id))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Artista no encontrado"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }

        delete("/{id}") {
            try {
                val id = call.parameters["id"]!!
                if (ArtistaService.delete(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Artista no encontrado"))
                }
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(e.message ?: "Error"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }
    }
}