package ru.example.rickandmortyproject.domain.characters.list.model

import java.io.Serializable

data class SingleCharacterDomain(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val url: String,
    val created: String
):Serializable