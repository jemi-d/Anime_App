package com.example.animepoc.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animepoc.di.AnimeRepository
import com.example.animepoc.local.AnimeDetailsEntity
import com.example.animepoc.local.AnimeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {

    private val _animeList = MutableLiveData<List<AnimeEntity>>()
    val animeList: LiveData<List<AnimeEntity>> get() = _animeList

    private val _animeDetails = MutableLiveData<AnimeDetailsEntity?>()
    val animeDetails: LiveData<AnimeDetailsEntity?> get() = _animeDetails

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchTopAnime() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = repository.getTopAnime()
                _animeList.postValue(list)
                Log.d("AnimeViewModel", "fetched top anime: size=${list.size}")
            } catch (e: Exception) {
                _error.postValue(e.message)
                Log.e("AnimeViewModel", "fetchTopAnime failed", e)
            }
        }
    }

    fun fetchAnimeDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val details = repository.getAnimeDetails(id)
                _animeDetails.postValue(details)
                Log.d("AnimeViewModel", "fetched details for id=$id: ${details != null}")
            } catch (e: Exception) {
                _error.postValue(e.message)
                Log.e("AnimeViewModel", "fetchAnimeDetails failed", e)
            }
        }
    }
}
