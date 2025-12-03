package com.backend.services

import com.backend.models.Album
import com.backend.tables.Albumes
import com.backend.tables.Tracks
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object AlbumService {
    fun create(dto: Album): Album = transaction {
        val id = Albumes.insertAndGetId {
            it[title] = dto.title
            it[releaseYear] = dto.releaseYear
            it[artistId] = UUID.fromString(dto.artistId)
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }
        dto.copy(id = id.toString())
    }

    fun getAll(): List<Album> = transaction {
        Albumes.selectAll().map {
            Album(
                id = it[Albumes.id].toString(),
                title = it[Albumes.title],
                releaseYear = it[Albumes.releaseYear],
                artistId = it[Albumes.artistId].toString()
            )
        }
    }

    fun getById(id: String): Album? = transaction {
        Albumes.select { Albumes.id eq UUID.fromString(id) }
            .singleOrNull()?.let {
                Album(
                    id = it[Albumes.id].toString(),
                    title = it[Albumes.title],
                    releaseYear = it[Albumes.releaseYear],
                    artistId = it[Albumes.artistId].toString()
                )
            }
    }

    fun update(id: String, dto: Album): Boolean = transaction {
        Albumes.update({ Albumes.id eq UUID.fromString(id) }) {
            it[title] = dto.title
            it[releaseYear] = dto.releaseYear
            it[artistId] = UUID.fromString(dto.artistId)
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: String): Boolean = transaction {
        val hasTracks = Tracks.select { Tracks.albumId eq UUID.fromString(id) }
            .count() > 0
        if (hasTracks) {
            throw IllegalStateException("No se puede eliminar: el álbum tiene tracks asociadas")
        }
        Albumes.deleteWhere { Albumes.id eq UUID.fromString(id) } > 0
    }
}