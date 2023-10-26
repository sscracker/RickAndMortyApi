package ru.example.rickandmortyproject.domain.characters.list

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity

interface CharactersRepository {
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    suspend fun loadCharacterPage(pageNumber: Int): Boolean

    fun getCharacter(characterId: Int): Flow<CharacterEntity>

    fun getCharactersById(ids: List<Int>): Flow<List<CharacterEntity>>

    suspend fun loadCharactersById(ids: List<Int>): Boolean

    suspend fun getFilterSettings(): CharacterFilterSettings

    suspend fun saveFilterSettings(settings: CharacterFilterSettings): Boolean
}