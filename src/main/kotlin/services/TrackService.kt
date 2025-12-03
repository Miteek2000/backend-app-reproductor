package com.backend.services

import com.backend.models.Track
import com.backend.tables.Tracks
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object TrackService {
    fun create(dto: Track): Track = transaction {
        val id = Tracks.insertAndGetId {
            it[title] = dto.title
            it[duration] = dto.duration
            it[albumId] = UUID.fromString(dto.albumId)
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }
        dto.copy(id = id.toString())
    }

    fun getAll(): List<Track> = transaction {
        Tracks.selectAll().map {
            Track(
                id = it[Tracks.id].toString(),
                title = it[Tracks.title],
                duration = it[Tracks.duration],
                albumId = it[Tracks.albumId].toString()
            )
        }
    }

    fun getById(id: String): Track? = transaction {
        Tracks.select { Tracks.id eq UUID.fromString(id) }
            .singleOrNull()?.let {
                Track(
                    id = it[Tracks.id].toString(),
                    title = it[Tracks.title],
                    duration = it[Tracks.duration],
                    albumId = it[Tracks.albumId].toString()
                )
            }
    }

    fun update(id: String, dto: Track): Boolean = transaction {
        Tracks.update({ Tracks.id eq UUID.fromString(id) }) {
            it[title] = dto.title
            it[duration] = dto.duration
            it[albumId] = UUID.fromString(dto.albumId)
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: String): Boolean = transaction {
        Tracks.deleteWhere { Tracks.id eq UUID.fromString(id) } > 0
    }
}