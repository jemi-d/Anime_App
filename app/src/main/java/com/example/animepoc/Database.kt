package com.example.animepoc

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.animepoc.local.AnimeDao
import com.example.animepoc.local.AnimeDetailsEntity
import com.example.animepoc.local.AnimeEntity

@Database(entities = [AnimeEntity::class, AnimeDetailsEntity::class], version = 1, exportSchema = false)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object {
        @Volatile
        private var INSTANCE: AnimeDatabase? = null

        fun getDatabase(context: Context): AnimeDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDatabase::class.java,
                    "anime_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
