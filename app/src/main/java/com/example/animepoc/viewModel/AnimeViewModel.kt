package com.example.animepoc.viewModel

import Anime
import AnimeDetails
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animepoc.di.AnimeRepository
import com.example.animepoc.local.AnimeDetailsEntity
import com.example.animepoc.local.AnimeEntity
import kotlinx.coroutines.launch

//class AnimeViewModel(private val repository: ApiRepository) : ViewModel() {
//
//    private val _animeList = MutableLiveData<List<Anime>>()
//    val animeList: LiveData<List<Anime>> get() = _animeList
//
//    private val _animeDetails = MutableLiveData<AnimeDetails>()
//    val animeDetails: LiveData<AnimeDetails> get() = _animeDetails
//
//    private val _error = MutableLiveData<String>()
//    val error: LiveData<String> get() = _error
//
//    fun fetchTopAnime() {
//        viewModelScope.launch {
//            try {
//                _animeList.value = repository.getTopAnime()
//            } catch (e: Exception) {
//                _error.value = e.message
//            }
//        }
//    }
//
//    fun fetchAnimeDetails(id: Int) {
//        viewModelScope.launch {
//            try {
//                _animeDetails.value = repository.getAnimeDetails(id)
//            } catch (e: Exception) {
//                _error.value = e.message
//            }
//        }
//    }
//}


class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {

    private val _animeList = MutableLiveData<List<AnimeEntity>>()
    val animeList: LiveData<List<AnimeEntity>> get() = _animeList

    private val _animeDetails = MutableLiveData<AnimeDetailsEntity?>()
    val animeDetails: LiveData<AnimeDetailsEntity?> get() = _animeDetails

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchTopAnime() {
        viewModelScope.launch {
            try {
                _animeList.value = repository.getTopAnime()
                println("the response${_animeList.value}")
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun fetchAnimeDetails(id: Int) {
        viewModelScope.launch {
            try {
                _animeDetails.value = repository.getAnimeDetails(id)
                println("the response details ${_animeDetails.value}")
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
