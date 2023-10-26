package ru.example.rickandmortyproject.data.characters.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.example.rickandmortyproject.data.characters.list.api.CharactersApi
import ru.example.rickandmortyproject.data.characters.list.mapper.CharacterDataToListSingleCharacterDomainMapper
import ru.example.rickandmortyproject.data.db.lists.CharacterListDao
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.model.SingleCharacterDomain
import ru.example.rickandmortyproject.utils.Response
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val charactersApi: CharactersApi,
    private val mapper: CharacterDataToListSingleCharacterDomainMapper,
    private val characterListDao: CharacterListDao
) : CharactersRepository {
    override fun getAllCharacters(): Flow<Response<List<SingleCharacterDomain>>> {
        return flow {
            emit(
                kotlin.runCatching{
                    val response = charactersApi.getAllCharacters()
                    characterListDao.setCharacterList(response.body()?.characters ?: emptyList())
                    Response.Success(
                        mapper.map(response.body()?.characters ?: emptyList())
                    )
                }.getOrThrow()
            )
        }
    }

    override fun getAllCharactersFromLocal(): Flow<List<SingleCharacterDomain>> {
        return flow {
            emit(
                kotlin.runCatching {
                    val response = characterListDao.getCharacterList()
                    mapper.map(response)
                }.getOrDefault(emptyList())
            )
        }
    }
}