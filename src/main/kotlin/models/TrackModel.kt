package com.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val id: String? = null,
    val title: String,
    val duration: Int,
    val albumId: String
)