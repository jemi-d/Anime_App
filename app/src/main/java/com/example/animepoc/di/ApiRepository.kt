package com.example.animepoc.di

import Anime
import AnimeDetails
import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import com.example.animepoc.local.AnimeDao
import com.example.animepoc.local.AnimeDetailsEntity
import com.example.animepoc.local.AnimeEntity

//class ApiRepository(private val api: ApiService) {
//
//    suspend fun getTopAnime(): List<Anime> {
//        val response = api.getTopAnime()
//        if (response.isSuccessful) {
//            return response.body()?.data ?: emptyList()
//        } else {
//            throw Exception("Failed to fetch data")
//        }
//    }
//
//    suspend fun getAnimeDetails(id: Int): AnimeDetails {
//        val response = api.getAnimeDetails(id)
//        if (response.isSuccessful) {
//            return response.body() ?: throw Exception("Details not found")
//        } else {
//            throw Exception("Failed to fetch details")
//        }
//    }
//}

class AnimeRepository(
    private val api: ApiService,
    private val dao: AnimeDao,
    private val context: Context
) {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }

    suspend fun getTopAnime(): List<AnimeEntity> {
        return if (isConnected()) {
            val response = api.getTopAnime()
            if (response.isSuccessful) {
                val animeList = response.body()?.data?.map {
                    AnimeEntity(
                        malId = it.mal_id,
                        title = it.title,
                        episodes = it.episodes,
                        rating = it.rating,
                        imageUrl = it.images.jpg.image_url,
                        largeImageUrl = it.images.jpg.large_image_url
                    )
                } ?: emptyList()

                dao.insertAnimeList(animeList) // cache
                animeList
            } else {
                dao.getAllAnime()
            }
        } else {
            dao.getAllAnime() // offline
        }
    }

    suspend fun getAnimeDetails(id: Int): AnimeDetailsEntity? {
        return if (isConnected()) {
            val response = api.getAnimeDetails(id)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    val details = AnimeDetailsEntity(
                        malId = it.mal_id,
                        title = it.title,
                        episodes = it.episodes,
                        rating = it.rating,
                        score = it.score,
                        rank = it.rank,
                        favorites = it.favorites,
                        imageUrl = it.images.jpg.image_url,
                        trailerUrl = it.trailer.url,
                        genres = it.genres.joinToString(",") { g -> g.name }
                    )
                    dao.insertAnimeDetails(details)
                    details
                }
            } else {
                dao.getAnimeDetails(id)
            }
        } else {
            dao.getAnimeDetails(id)
        }
    }
}
