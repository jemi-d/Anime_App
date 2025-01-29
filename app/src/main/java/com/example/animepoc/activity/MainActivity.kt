package com.example.animepoc.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.animepoc.R
import com.example.animepoc.adapter.AnimeAdapter
import com.example.animepoc.databinding.ActivityMainBinding
import com.example.animepoc.di.ApiRepository
import com.example.animepoc.di.ApiService
import com.example.animepoc.di.Network
import com.example.animepoc.viewModel.AnimeViewModel
import com.example.animepoc.viewModel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var adapter: AnimeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = AnimeAdapter { selectedAnime ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("ITEM_ID", selectedAnime.mal_id)
            startActivity(intent)
        }
        val spanCount = getSpanCount()
        binding.animeListView.layoutManager = GridLayoutManager(this, spanCount)
        binding.animeListView.adapter = adapter

        val repository = ApiRepository(Network.retrofit.create(ApiService::class.java))
        val viewModelFactory = ViewModelFactory(repository)
        animeViewModel = ViewModelProvider(this, viewModelFactory)[AnimeViewModel::class.java]
        animeViewModel.animeList.observe(this, { animeList ->
            binding.homeProgressBar.visibility = View.GONE
            if (animeList.isNotEmpty()) {
                animeList?.let {
                    adapter.setAnimeList(it)
                }
            }
        })
        animeViewModel.fetchTopAnime()
    }

    private fun getSpanCount(): Int {
        return if (resources.configuration.smallestScreenWidthDp >= 600) {
            4 // Tablet
        } else {
            2 // Mobile
        }
    }

}