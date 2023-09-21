package ru.example.rickandmortyproject.domain.characters.list

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.characters.list.model.SingleCharacterDomain
import ru.example.rickandmortyproject.utils.Response

interface CharactersListUseCase {
    fun getAllCharacters(): Flow<Response<List<SingleCharacterDomain>>>

    fun getAllCharactersFromLocal(): Flow<List<SingleCharacterDomain>>
}