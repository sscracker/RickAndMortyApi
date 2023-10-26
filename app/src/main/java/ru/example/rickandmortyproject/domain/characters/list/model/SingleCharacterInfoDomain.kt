package ru.example.rickandmortyproject.domain.characters.list.model

import java.io.Serializable

data class SingleCharacterInfoDomain(
    val count: String,
    val pages: String,
    val next: String,
    val previous: String
): Serializable