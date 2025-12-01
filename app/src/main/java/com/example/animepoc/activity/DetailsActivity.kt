package com.example.animepoc.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
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
        val id = intent.getIntExtra("ITEM_ID", -1)

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
            // Hide progress whenever we have a result (null or not)
            binding.detailsProgressBar.visibility = View.GONE

            if (detailsValue == null) {
                // No details found in local DB (likely offline)
                binding.offlineNotFoundText.visibility = View.VISIBLE

                // Hide content views that depend on details
                binding.detailsPosterImage.visibility = View.GONE
                binding.detailsTitle.visibility = View.GONE
                binding.detailsEpisodeNumber.visibility = View.GONE
                binding.detailsRating.visibility = View.GONE
                binding.detailsSynopsis.visibility = View.GONE
                binding.detailsGenresTitle.visibility = View.GONE
                binding.detailsGenresValues.visibility = View.GONE
                binding.playIcon.visibility = View.GONE
                safeHideWebView()
            } else {
                // Have details -> hide offline message and show content
                binding.offlineNotFoundText.visibility = View.GONE
                showContentViews()
                setDetails(detailsValue)
            }
        }

        // Fetch details
        if (id != -1) {
            binding.detailsProgressBar.visibility = View.VISIBLE
            binding.offlineNotFoundText.visibility = View.GONE
            animeViewModel.fetchAnimeDetails(id)
        }
    }

    private fun showContentViews() {
        binding.detailsPosterImage.visibility = View.VISIBLE
        binding.detailsTitle.visibility = View.VISIBLE
        binding.detailsEpisodeNumber.visibility = View.VISIBLE
        binding.detailsRating.visibility = View.VISIBLE
        binding.detailsSynopsis.visibility = View.VISIBLE
        // genres may be toggled in setDetails
    }

    private fun setDetails(value: AnimeDetailsEntity) {
        // Use Glide's clear before loading to cancel any previous requests
        Glide.with(this).clear(binding.detailsPosterImage)
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
        if (value.genres.isNotEmpty()) {
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

                // Enabling JavaScript can introduce XSS vulnerabilities; this app only loads
                // trusted trailer URLs from the API. Review if URLs may be untrusted.
                val webSettings: WebSettings = binding.webView.settings
                webSettings.javaScriptEnabled = true
                // Use the existing WebView instance safely
                binding.webView.webViewClient = WebViewClient()
                binding.webView.loadUrl(value.trailerUrl)
                binding.playIcon.visibility = View.GONE
                binding.webView.visibility = View.VISIBLE
            }
        } else {
            binding.playIcon.visibility = View.GONE
            safeHideWebView()
        }
    }

    private fun safeHideWebView() {
        // Stop loading and hide WebView without destroying it here
        try {
            binding.webView.stopLoading()
            binding.webView.visibility = View.GONE
        } catch (_: Exception) {
            // ignore, defensive
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear any pending Glide requests tied to the image view
        try {
            Glide.with(this).clear(binding.detailsPosterImage)
        } catch (_: Exception) {
            // ignore
        }

        // Properly destroy the WebView to avoid leaks
        try {
            val webView: WebView = binding.webView
            webView.apply {
                clearHistory()
                loadUrl("about:blank")
                onPause()
                removeAllViews()
                destroy()
            }
        } catch (_: Exception) {
            // ignore
        }

        // Avoid holding references from data binding
        binding.detailsPosterImage.setImageDrawable(null)
        binding.detailsTitle.text = null
        binding.detailsEpisodeNumber.text = null
        binding.detailsRating.text = null
        binding.detailsSynopsis.text = null
        binding.detailsGenresValues.text = null
        binding.playIcon.setImageDrawable(null)
    }
}