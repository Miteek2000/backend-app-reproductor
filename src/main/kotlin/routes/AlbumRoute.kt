package com.backend.routes


import com.backend.models.Album
import com.backend.models.ErrorResponse
import com.backend.services.AlbumService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.albumRoutes() {
    route("/albumes") {
        post {
            try {
                val dto = call.receive<Album>()
                val created = AlbumService.create(dto)
                call.respond(HttpStatusCode.Created, created)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }

        get {
            call.respond(AlbumService.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]!!
            val album = AlbumService.getById(id)
            if (album != null) {
                call.respond(HttpStatusCode.OK, album)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Álbum no encontrado"))
            }
        }

        put("/{id}") {
            try {
                val id = call.parameters["id"]!!
                val dto = call.receive<Album>()
                if (AlbumService.update(id, dto)) {
                    call.respond(HttpStatusCode.OK, dto.copy(id = id))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Álbum no encontrado"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }

        delete("/{id}") {
            try {
                val id = call.parameters["id"]!!
                if (AlbumService.delete(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Álbum no encontrado"))
                }
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(e.message ?: "Error"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error"))
            }
        }
    }
}