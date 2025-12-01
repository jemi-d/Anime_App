package com.example.animepoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animepoc.R
import com.example.animepoc.local.AnimeEntity
import com.google.android.material.imageview.ShapeableImageView

class AnimeAdapter(
    private val onItemClick: (AnimeEntity) -> Unit
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    private val animeList = mutableListOf<AnimeEntity>()

    fun setAnimeList(list: List<AnimeEntity>) {
        animeList.clear()
        animeList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_item_view, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.bind(anime)
    }

    override fun getItemCount(): Int = animeList.size

    inner class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.list_title)
        private val posterImage: ShapeableImageView = itemView.findViewById(R.id.list_poster_image)
        private val itemEpisode: TextView = itemView.findViewById(R.id.list_episode_number)
        private val itemRating: TextView = itemView.findViewById(R.id.list_rating)

        fun bind(anime: AnimeEntity) {
            titleText.text = anime.title
            itemEpisode.text = anime.episodes.toString()
            itemRating.text = anime.rating
            Glide.with(itemView.context)
                .load(anime.imageUrl)
                .into(posterImage)

            itemView.setOnClickListener { onItemClick(anime) }
        }
    }
}