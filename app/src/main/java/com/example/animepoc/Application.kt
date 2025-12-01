package com.example.animepoc

import android.app.Application
import com.example.animepoc.di.AnimeRepository
import com.example.animepoc.di.ApiService
import com.example.animepoc.di.Network

class MyApp : Application() {
    lateinit var repository: AnimeRepository

    override fun onCreate() {
        super.onCreate()
        val database = AnimeDatabase.getDatabase(this)
        repository = AnimeRepository(Network.retrofit.create(ApiService::class.java),
            database.animeDao(), this)
    }
}
