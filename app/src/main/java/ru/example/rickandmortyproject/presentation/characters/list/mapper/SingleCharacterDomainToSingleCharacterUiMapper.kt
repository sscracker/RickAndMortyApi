package ru.example.rickandmortyproject.presentation.characters.list.mapper

import ru.example.rickandmortyproject.domain.characters.list.model.SingleCharacterDomain
import ru.example.rickandmortyproject.presentation.characters.list.model.SingleCharacter

class SingleCharacterDomainToSingleCharacterUiMapper {
    fun map(from: List<SingleCharacterDomain>): List<SingleCharacter>{
        return from.map {
            SingleCharacter(
                id = it.id.toInt(),
                name = it.name,
                image = it.image,
                gender = it.gender,
                status = it.status,
                species = it.species
            )
        }
    }
}