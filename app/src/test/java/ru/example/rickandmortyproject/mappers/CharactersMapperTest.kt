package ru.example.rickandmortyproject.mappers

import com.google.gson.JsonArray
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.example.rickandmortyproject.data.characters.mapper.CharactersMapper
import ru.example.rickandmortyproject.data.characters.model.CharacterDbModel
import ru.example.rickandmortyproject.data.characters.model.CharacterDto
import ru.example.rickandmortyproject.data.characters.model.LocationMinDto
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterGender
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterStatus

class CharactersMapperTest {
    private lateinit var charactersMapper: CharactersMapper

    @Before
    fun setup() {
        charactersMapper = CharactersMapper()
    }

    @Test
    fun `GIVEN dto WHEN map dto to dbmodel THEN map correctly`() {
        val dto = createSampleCharacterDto()

        val result = charactersMapper.mapDtoToDbModel(dto)

        assertEquals(123, result.id)
        assertEquals("Morty Smith", result.name)
        assertEquals(CharacterStatus.ALIVE.ordinal, result.status)
        assertEquals("Human", result.species)
        assertEquals("Teenager", result.type)
        assertEquals(CharacterGender.MALE.ordinal, result.gender)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/123.jpeg", result.image)
        assertEquals("https://rickandmortyapi.com/api/character/123", result.url)
        assertEquals(1, result.originId)
        assertEquals(20, result.locationId)
    }

    @Test
    fun `GIVEN dbmodel WHEN map dbmodel to domain THEN map correctly`() {
        val dbModel = createSampleCharacterDbModel()

        val result = charactersMapper.mapDbModelToDomain(dbModel)

        assertEquals(123, result.id)
        assertEquals("Morty Smith", result.name)
        assertEquals(CharacterStatus.ALIVE, result.status)
        assertEquals("Human", result.species)
        assertEquals("Teenager", result.type)
        assertEquals(CharacterGender.MALE, result.gender)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/123.jpeg", result.image)
        assertEquals("https://rickandmortyapi.com/api/character/123", result.url)
        assertEquals(1, result.originId)
        assertEquals(20, result.locationId)
        assertEquals(listOf(1, 2), result.episodesId)
    }

    private fun createSampleCharacterDbModel(): CharacterDbModel {
        return CharacterDbModel(
            id = 123,
            name = "Morty Smith",
            status = CharacterStatus.ALIVE.ordinal,
            species = "Human",
            type = "Teenager",
            gender = CharacterGender.MALE.ordinal,
            image = "https://rickandmortyapi.com/api/character/avatar/123.jpeg",
            url = "https://rickandmortyapi.com/api/character/123",
            created = "2017-11-04T18:48:46.250Z",
            originId = 1,
            locationId = 20,
            episodesId = "1,2"
        )
    }

    private fun createSampleCharacterDto(): CharacterDto {
        return CharacterDto(
            id = 123,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            type = "Teenager",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/123.jpeg",
            url = "https://rickandmortyapi.com/api/character/123",
            created = "2017-11-04T18:48:46.250Z",
            origin =
            LocationMinDto(
                name = "Earth (C-137)",
                url = "https://rickandmortyapi.com/api/location/1"
            ),
            location =
            LocationMinDto(
                name = "Earth (Replacement Dimension)",
                url = "https://rickandmortyapi.com/api/location/20"
            ),
            episode =
            JsonArray().apply {
                add("https://rickandmortyapi.com/api/episode/1")
                add("https://rickandmortyapi.com/api/episode/2")
            }
        )
    }
}
