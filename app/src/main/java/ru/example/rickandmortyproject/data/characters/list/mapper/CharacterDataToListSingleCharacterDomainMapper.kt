package ru.example.rickandmortyproject.data.characters.list.mapper

import ru.example.rickandmortyproject.data.characters.list.model.SingleCharacterEntity
import ru.example.rickandmortyproject.domain.characters.list.model.SingleCharacterDomain

class CharacterDataToListSingleCharacterDomainMapper {
    fun map(from: List<SingleCharacterEntity>) = from.map {
        SingleCharacterDomain(
            id = it.id,
            name = it.name,
            status = it.status,
            species = it.species,
            gender = it.gender,
            type = it.type,
            created = it.created,
            image = it.image,
            url = it.url
        )
    }
}