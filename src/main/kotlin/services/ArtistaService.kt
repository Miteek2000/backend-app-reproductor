package com.backend.services

import com.backend.models.Artista
import com.backend.tables.Albumes
import com.backend.tables.Artistas
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object ArtistaService {
    fun create(dto: Artista): Artista = transaction {
        val id = Artistas.insertAndGetId {
            it[name] = dto.name
            it[genre] = dto.genre
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }
        dto.copy(id = id.toString())
    }

    fun getAll(): List<Artista> = transaction {
        Artistas.selectAll().map {
            Artista(
                id = it[Artistas.id].toString(),
                name = it[Artistas.name],
                genre = it[Artistas.genre]
            )
        }
    }

    fun getById(id: String): Artista? = transaction {
        Artistas.select { Artistas.id eq UUID.fromString(id) }
            .singleOrNull()?.let {
                Artista(
                    id = it[Artistas.id].toString(),
                    name = it[Artistas.name],
                    genre = it[Artistas.genre]
                )
            }
    }

    fun update(id: String, dto: Artista): Boolean = transaction {
        Artistas.update({ Artistas.id eq UUID.fromString(id) }) {
            it[name] = dto.name
            it[genre] = dto.genre
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: String): Boolean = transaction {
        val hasAlbums = Albumes.select { Albumes.artistId eq UUID.fromString(id) }
            .count() > 0
        if (hasAlbums) {
            throw IllegalStateException("No se puede eliminar: el artista tiene álbumes asociados")
        }
        Artistas.deleteWhere { Artistas.id eq UUID.fromString(id) } > 0
    }
}