package com.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class Artista(
    val id: String? = null,
    val name: String,
    val genre: String? = null
)