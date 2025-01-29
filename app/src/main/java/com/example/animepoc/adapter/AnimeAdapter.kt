package com.example.animepoc.adapter

import Anime
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animepoc.R
import com.google.android.material.imageview.ShapeableImageView

class AnimeAdapter(private val onItemClicked: (Anime) -> Unit) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    private val animeList = mutableListOf<Anime>()

    fun setAnimeList(newList: List<Anime>) {
        animeList.clear()
        animeList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimeAdapter.AnimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_item_view, parent, false)
        return AnimeViewHolder(view)    }

    override fun onBindViewHolder(holder: AnimeAdapter.AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.bind(anime, onItemClicked)
    }

    override fun getItemCount(): Int = animeList.size

    class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ShapeableImageView = itemView.findViewById(R.id.list_poster_image)
        private val itemTitle: TextView = itemView.findViewById(R.id.list_title)
        private val itemEpisode: TextView = itemView.findViewById(R.id.list_episode_number)
        private val itemRating: TextView = itemView.findViewById(R.id.list_rating)

        fun bind(anime: Anime, onItemClicked: (Anime) -> Unit) {
            itemTitle.text = anime.title
            itemEpisode.text = anime.episodes.toString()
            itemRating.text = anime.rating
            Glide.with(itemView.context)
                .load(anime.images.jpg.image_url)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClicked(anime)
            }
        }
    }
}