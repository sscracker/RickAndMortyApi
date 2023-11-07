package ru.example.rickandmortyproject.presentation.episodes.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity

class EpisodesListAdapter(
    private val onListEnded: (() -> Unit)?,
    private val onItemClick: (EpisodeEntity) -> Unit
) : ListAdapter<EpisodeEntity, EpisodesListAdapter.EpisodesListViewHolder>(EpisodesDiffCallback) {

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

    override fun onBindViewHolder(holder: EpisodesListViewHolder, position: Int) {
        val entity = getItem(position)
        holder.name.text = entity.name
        holder.code.text = entity.episodeCode
        holder.airDate.text = entity.airDate
        holder.itemView.setOnClickListener {
            onItemClick.invoke(entity)
        }
        if (position == currentList.size - 1) {
            onListEnded?.invoke()
        }
    }
}
