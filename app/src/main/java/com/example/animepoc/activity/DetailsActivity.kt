package com.example.animepoc.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.animepoc.MyApp
import com.example.animepoc.R
import com.example.animepoc.databinding.ActivityDetailsBinding
import com.example.animepoc.local.AnimeDetailsEntity
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
        val repository = (application as MyApp).repository
        val factory = ViewModelFactory(repository)
        animeViewModel = ViewModelProvider(this, factory)[AnimeViewModel::class.java]

        // Observe LiveData
        animeViewModel.animeDetails.observe(this) { detailsValue ->
            detailsValue?.let {
                setDetails(it)
                binding.detailsProgressBar.visibility = View.GONE
            }
        }

        // Fetch details
        if (id != -1) {
            binding.detailsProgressBar.visibility = View.VISIBLE
            animeViewModel.fetchAnimeDetails(id)
        }
    }

    private fun setDetails(value: AnimeDetailsEntity) {
        Glide.with(binding.detailsPosterImage)
            .load(value.imageUrl)
            .into(binding.detailsPosterImage)
        binding.detailsTitle.text = value.title
        binding.detailsEpisodeNumber.text = value.episodes.toString()
        binding.detailsRating.text = buildString {
            append("|  ")
            append(value.rating)
        }
        binding.detailsSynopsis.text = value.favorites.toString()
        if (!value.genres.isNullOrEmpty()) {
            val genreTitles = value.genres
            binding.detailsGenresValues.text = genreTitles
            binding.detailsGenresTitle.visibility = View.VISIBLE
            binding.detailsGenresValues.visibility = View.VISIBLE
        } else {
            binding.detailsGenresTitle.visibility = View.GONE
            binding.detailsGenresValues.visibility = View.GONE
        }

        if (!value.trailerUrl.isNullOrEmpty()) {
            binding.playIcon.visibility = View.VISIBLE
            binding.playIcon.setOnClickListener {

                val webSettings: WebSettings = binding.webView.settings
                webSettings.javaScriptEnabled = true
                binding.webView.webViewClient = WebViewClient()
                binding.webView.loadUrl(value.trailerUrl)
                binding.playIcon.visibility = View.GONE
                binding.webView.visibility = View.VISIBLE
            }
        }
    }
}