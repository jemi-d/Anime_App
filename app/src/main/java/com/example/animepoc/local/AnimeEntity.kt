package com.example.animepoc.local

import androidx.room.*

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val episodes: Int?,
    val rating: String?,
    val imageUrl: String,
    val largeImageUrl: String
)

@Entity(tableName = "anime_details")
data class AnimeDetailsEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val episodes: Int,
    val rating: String,
    val score: Double,
    val rank: Int,
    val favorites: Int,
    val imageUrl: String,
    val trailerUrl: String?,
    val genres: String // store as CSV or JSON string
)
