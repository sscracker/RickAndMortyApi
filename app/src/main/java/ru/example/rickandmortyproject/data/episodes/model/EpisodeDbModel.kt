package ru.example.rickandmortyproject.data.episodes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EpisodesListEntity")
data class EpisodeDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characterId: String,
    val url: String,
    val created: String
)
