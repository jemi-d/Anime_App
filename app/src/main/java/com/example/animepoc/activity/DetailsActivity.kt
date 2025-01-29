package com.example.animepoc.activity

import AnimeDetails
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.animepoc.R
import com.example.animepoc.databinding.ActivityDetailsBinding
import com.example.animepoc.di.ApiRepository
import com.example.animepoc.di.ApiService
import com.example.animepoc.di.Network
import com.example.animepoc.viewModel.AnimeViewModel
import com.example.animepoc.viewModel.ViewModelFactory

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var animeViewModel: AnimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val id = intent.getIntExtra("ITEM_ID", 0)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // API
        val repository = ApiRepository(Network.retrofit.create(ApiService::class.java))
        val viewModelFactory = ViewModelFactory(repository)
        animeViewModel = ViewModelProvider(this, viewModelFactory)[AnimeViewModel::class.java]
        animeViewModel.animeDetails.observe(this, { detailsValue ->
            setDetails(detailsValue)
            binding.detailsProgressBar.visibility = View.GONE
        })
        animeViewModel.fetchAnimeDetails(id)
    }

    private fun setDetails(value : AnimeDetails) {
        Glide.with(binding.detailsPosterImage)
            .load(value.data.images.jpg.large_image_url)
            .into(binding.detailsPosterImage)
        binding.detailsTitle.text = value.data.title
        binding.detailsEpisodeNumber.text = value.data.episodes.toString()
        binding.detailsRating.text = buildString {
            append("|  ")
            append(value.data.rating)
        }
        binding.detailsSynopsis.text = value.data.synopsis
        if (!value.data.genres.isNullOrEmpty()) {
            val genreTitles = value.data.genres.joinToString(", ") { it.name }
            binding.detailsGenresValues.text = genreTitles
            binding.detailsGenresTitle.visibility = View.VISIBLE
            binding.detailsGenresValues.visibility = View.VISIBLE
        } else {
            binding.detailsGenresTitle.visibility = View.GONE
            binding.detailsGenresValues.visibility = View.GONE
        }

        if (!value.data.trailer.url.isNullOrEmpty()) {
            binding.playIcon.visibility = View.VISIBLE
            binding.playIcon.setOnClickListener {

                val webSettings: WebSettings = binding.webView.settings
                webSettings.javaScriptEnabled = true
                binding.webView.webViewClient = WebViewClient()
                binding.webView.loadUrl(value.data.trailer.url)
                binding.playIcon.visibility = View.GONE
                binding.webView.visibility = View.VISIBLE
            }
        }
    }
}