package ru.example.rickandmortyproject.domain.characters.list.model

data class CharacterEntity(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val image: String,
    val url: String,
    val created: String,
    val originId: Int,
    val locationId: Int,
    val episodesId: List<Int>
)
