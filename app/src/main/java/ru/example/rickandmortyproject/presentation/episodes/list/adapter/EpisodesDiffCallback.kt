package ru.example.rickandmortyproject.presentation.episodes.list.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity

object EpisodesDiffCallback: DiffUtil.ItemCallback<EpisodeEntity>() {
    override fun areItemsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem == newItem
    }
}