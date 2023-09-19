package ru.example.rickandmortyproject.domain.characters.list

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.characters.list.model.SingleCharacterDomain
import ru.example.rickandmortyproject.utils.ResponseUtil


interface CharactersRepository {
    fun getAllCharacters(): Flow<ResponseUtil<List<SingleCharacterDomain>>>
    fun getAllCharactersFromLocal(): Flow<List<SingleCharacterDomain>>
}