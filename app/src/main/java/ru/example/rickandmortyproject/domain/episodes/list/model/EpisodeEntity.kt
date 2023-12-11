package ru.example.rickandmortyproject.domain.episodes.list.model

data class EpisodeEntity(
    val id: Int,
    val name: String,
    val airDate: String,
    val episodeCode: String,
    val charactersId: List<Int>,
    val url: String,
    val created: String,
)
