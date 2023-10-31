package ru.example.rickandmortyproject.data.characters

import com.google.gson.Gson
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import ru.example.rickandmortyproject.data.characters.api.CharactersApi
import ru.example.rickandmortyproject.data.characters.mapper.CharactersMapper
import ru.example.rickandmortyproject.data.db.lists.CharacterListDao
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings
import ru.example.rickandmortyproject.utils.Preferences

class CharactersRepositoryImpl @Inject constructor(
    preferences: Preferences,
    private val charactersApi: CharactersApi,
    private val mapper: CharactersMapper,
    private val characterListDao: CharacterListDao
) : CharactersRepository {

    private val preferences = preferences.getCharacterRepositoryPreferences()

    override fun getAllCharacters() = characterListDao.getCharacterList().map {
        mapper.mapDbModelsListToEntitiesList(it)
    }

    override suspend fun loadCharacterPage(pageNumber: Int): Boolean {
        return try {
            val pageDto = charactersApi.loadPage(pageNumber)
            characterListDao.insertList(
                mapper.mapPageToDbModelList(pageDto)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    override fun getCharacter(characterId: Int) =
        characterListDao.getItem(characterId).map {
            mapper.mapDbModelToDomain(it)
        }

    override fun getCharactersById(ids: List<Int>) =
        characterListDao.getItemsByIds(ids).map {
            mapper.mapDbModelsListToEntitiesList(it)
        }

    override suspend fun loadCharactersById(ids: List<Int>): Boolean {
        if (ids.isEmpty()) {
            return true
        }
        return try {
            val characters = charactersApi.loadItemsByIds(ids.joinToString(","))
            characterListDao.insertList(
                mapper.mapDtoListToDbModelList(characters)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    override suspend fun getFilterSettings(): CharacterFilterSettings {
        val json = preferences.getString(KEY_CHARACTERS_FILTER, null)
        return json?.let {
            Gson().fromJson(json, CharacterFilterSettings::class.java)
        } ?: CharacterFilterSettings(
            EMPTY_VALUE,
            null,
            EMPTY_VALUE,
            EMPTY_VALUE,
            null
        )
    }

    override suspend fun saveFilterSettings(settings: CharacterFilterSettings): Boolean {
        val json = Gson().toJson(settings)
        preferences.edit().putString(KEY_CHARACTERS_FILTER, json).apply()
        return true
    }

    companion object {
        private const val KEY_CHARACTERS_FILTER = "charactersFilter"
        private const val EMPTY_VALUE = ""
    }
}
