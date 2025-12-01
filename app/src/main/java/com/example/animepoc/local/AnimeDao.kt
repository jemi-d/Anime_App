package com.example.animepoc.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnimeDao {

    // Anime list
    @Query("SELECT * FROM anime")
    suspend fun getAllAnime(): List<AnimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)

    // Anime details
    @Query("SELECT * FROM anime_details WHERE malId = :id LIMIT 1")
    suspend fun getAnimeDetails(id: Int): AnimeDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeDetails(details: AnimeDetailsEntity)
}
