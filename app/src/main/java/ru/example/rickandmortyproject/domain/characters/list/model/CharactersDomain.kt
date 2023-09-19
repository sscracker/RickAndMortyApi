package ru.example.rickandmortyproject.domain.characters.list.model

import java.io.Serializable

data class CharactersDomain(
    val info: SingleCharacterInfoDomain,
    val characters: List<SingleCharacterDomain>
): Serializable