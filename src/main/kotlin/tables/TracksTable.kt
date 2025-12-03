package com.backend.tables


import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Tracks : UUIDTable("tracks") {
    val title = varchar("title", 150)
    val duration = integer("duration")
    val albumId = uuid("album_id").references(Albumes.id)
    val createdAt = timestamp("created_at").default(Instant.now())
    val updatedAt = timestamp("updated_at").default(Instant.now())
}