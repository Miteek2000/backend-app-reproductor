package com.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: String? = null,
    val title: String,
    val releaseYear: Int,
    val artistId: String
)
