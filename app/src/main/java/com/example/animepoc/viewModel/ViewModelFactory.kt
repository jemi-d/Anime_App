package com.example.animepoc.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animepoc.di.AnimeRepository

//class ViewModelFactory(private val repository: ApiRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return AnimeViewModel(repository) as T
//    }
//}

class ViewModelFactory(private val repository: AnimeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnimeViewModel(repository) as T
    }
}
