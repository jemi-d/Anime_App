package com.example.animepoc.di

import Anime
import AnimeDetails

class ApiRepository(private val api: ApiService) {

    suspend fun getTopAnime(): List<Anime> {
        val response = api.getTopAnime()
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Failed to fetch data")
        }
    }

    suspend fun getAnimeDetails(id: Int): AnimeDetails {
        val response = api.getAnimeDetails(id)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Details not found")
        } else {
            throw Exception("Failed to fetch details")
        }
    }
}