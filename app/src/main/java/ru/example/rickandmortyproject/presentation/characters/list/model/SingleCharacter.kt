package ru.example.rickandmortyproject.presentation.characters.list.model

data class SingleCharacter(
    val id: Int,
    val image: String,
    val name: String,
    val gender: String,
    val species: String,
    val status: String
)
