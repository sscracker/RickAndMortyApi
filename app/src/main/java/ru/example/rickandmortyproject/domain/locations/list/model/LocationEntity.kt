package ru.example.rickandmortyproject.domain.locations.list.model

data class LocationEntity(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentsId: List<Int>,
    val url: String,
    val created: String,
)
