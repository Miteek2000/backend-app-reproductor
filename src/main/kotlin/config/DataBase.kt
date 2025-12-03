package com.backend.config

import com.backend.tables.Albumes
import com.backend.tables.Artistas
import com.backend.tables.Tracks
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.configureDatabases() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/app_music"
        driverClassName = "org.postgresql.Driver"
        username = "postgres"
        password = "Holasoymayte2006"
        maximumPoolSize = 10
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.create(Artistas, Albumes, Tracks)
    }
}
