package ru.example.rickandmortyproject.presentation.episodes.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity

class EpisodesListAdapter : Adapter<EpisodesListAdapter.EpisodesListViewHolder>() {

    private var episodesList = mutableListOf<EpisodeEntity>()

    class EpisodesListViewHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_episode_name_text_view)
        val code: TextView = itemView.findViewById(R.id.item_episode_code_text_view)
        val airDate: TextView = itemView.findViewById(R.id.item_episode_air_date_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_episode, parent, false)
            .let {
                EpisodesListViewHolder(it)
            }

    override fun getItemCount(): Int = episodesList.size

    override fun onBindViewHolder(holder: EpisodesListViewHolder, position: Int) {
        holder.name.text = "Episode_Name"
        holder.code.text = "Episode Code"
        holder.airDate.text = "Episode Airdate"
    }
}