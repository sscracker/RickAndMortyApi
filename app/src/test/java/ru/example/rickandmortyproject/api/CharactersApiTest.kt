package ru.example.rickandmortyproject.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.example.rickandmortyproject.data.characters.api.CharactersApi
import ru.example.rickandmortyproject.data.characters.api.ResponseDto
import ru.example.rickandmortyproject.data.characters.model.CharacterDto
import ru.example.rickandmortyproject.data.characters.model.LocationMinDto

class CharactersApiTest {
    @Mock
    lateinit var charactersApi: CharactersApi

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testLoadPage() {
        val page = 1
        val expectedResponse =
            ResponseDto(
                info = JsonObject(),
                results = JsonArray(),
            )
        runBlocking {
            `when`(charactersApi.loadPage(page)).thenReturn(expectedResponse)
            val actualResponse = charactersApi.loadPage(page)
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun testLoadItem() {
        val itemId = 1
        val expectedLocation =
            LocationMinDto(
                name = "Earth (Replacement Dimension)",
                url = "https://rickandmortyapi.com/api/location/20",
            )
        val expectedOrigin =
            LocationMinDto(
                name = "Earth (C-137)",
                url = "https://rickandmortyapi.com/api/location/1",
            )

        val episodesArray =
            JsonArray().apply {
                add(JsonPrimitive("https://rickandmortyapi.com/api/episode/1"))
                add(JsonPrimitive("https://rickandmortyapi.com/api/episode/2"))
            }

        val expectedCharacter =
            CharacterDto(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "Human with portal gun",
                gender = "Male",
                location = expectedLocation,
                origin = expectedOrigin,
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = episodesArray,
                url = "https://rickandmortyapi.com/api/character/1",
                created = "2017-11-04T18:48:46.250Z",
            )

        runBlocking {
            `when`(charactersApi.loadItem(itemId)).thenReturn(expectedCharacter)
            val actualCharacter = charactersApi.loadItem(itemId)
            assertEquals(expectedCharacter, actualCharacter)
        }
    }

    @Test
    fun testLoadItemsByIds() {
        val itemIds = "1,2,3"
        val expectedLocation =
            LocationMinDto(
                name = "Earth (Replacement Dimension)",
                url = "https://rickandmortyapi.com/api/location/20",
            )
        val expectedOrigin =
            LocationMinDto(
                name = "Earth (C-137)",
                url = "https://rickandmortyapi.com/api/location/1",
            )
        val episodesArray = JsonArray()
        episodesArray.add(JsonPrimitive("https://rickandmortyapi.com/api/episode/1"))
        episodesArray.add(JsonPrimitive("https://rickandmortyapi.com/api/episode/2"))

        val expectedCharacters =
            listOf(
                CharacterDto(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    type = "Human with portal gun",
                    gender = "Male",
                    location = expectedLocation,
                    origin = expectedOrigin,
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episode = episodesArray,
                    url = "https://rickandmortyapi.com/api/character/1",
                    created = "2017-11-04T18:48:46.250Z",
                ),
                CharacterDto(
                    id = 2,
                    name = "Morty Smith",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    location = expectedLocation,
                    origin = expectedOrigin,
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    episode = episodesArray,
                    url = "https://rickandmortyapi.com/api/character/2",
                    created = "2017-11-04T18:48:46.250Z",
                ),
                CharacterDto(
                    id = 3,
                    name = "Summer Smith",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Female",
                    location = expectedLocation,
                    origin = expectedOrigin,
                    image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
                    episode = episodesArray,
                    url = "https://rickandmortyapi.com/api/character/3",
                    created = "2017-11-04T18:48:46.250Z",
                ),
            )

        runBlocking {
            `when`(charactersApi.loadItemsByIds(itemIds)).thenReturn(expectedCharacters)
            val actualCharacters = charactersApi.loadItemsByIds(itemIds)
            assertEquals(expectedCharacters, actualCharacters)
        }
    }
}
