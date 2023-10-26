package ru.example.rickandmortyproject.domain.characters.list.model

data class CharacterFilterSettings(
    val name: String,
    val status: CharacterStatus?,
    val species: String,
    val type: String,
    val gender: CharacterGender?
)
