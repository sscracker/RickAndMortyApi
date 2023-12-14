package ru.example.rickandmortyproject.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.example.rickandmortyproject.data.characters.api.ResponseDto
import ru.example.rickandmortyproject.data.episodes.api.EpisodesApi
import ru.example.rickandmortyproject.data.episodes.model.EpisodesDto

class EpisodesApiTest {
    @Mock
    lateinit var episodesApi: EpisodesApi

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `GIVEN page WHEN load page THEN verify response`() {
        val page = 1
        val expectedResponse =
            ResponseDto(
                info = JsonObject(),
                results = JsonArray()
            )
        runBlocking {
            `when`(episodesApi.loadPage(page)).thenReturn(expectedResponse)
            val actualResponse = episodesApi.loadPage(page)
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `GIVEN item_id WHEN load item THEN verify episodes dto`() {
        val itemId = 123
        val expectedCharacters = JsonArray()
        val expectedEpisodesDto =
            EpisodesDto(
                itemId,
                "Sample Name",
                "2023-12-10",
                "S01E01",
                expectedCharacters,
                "https://example.com",
                "2023-12-10T14:30:00.000Z"
            )

        runBlocking {
            `when`(episodesApi.loadItem(itemId)).thenReturn(expectedEpisodesDto)
        }

        val actualEpisodesDto = runBlocking { episodesApi.loadItem(itemId) }

        assertEquals(expectedEpisodesDto, actualEpisodesDto)
    }

    @Test
    fun `GIVEN item_ids WHEN load episodes by ids THEN verify episodes list`() {
        val itemIds = "1,2,3"

        val episodesDto1 =
            EpisodesDto(
                1,
                "Episode 1",
                "2023-01-01",
                "S01E01",
                JsonArray(),
                "https://example.com/1",
                "2023-01-01T00:00:00Z"
            )
        val episodesDto2 =
            EpisodesDto(
                2,
                "Episode 2",
                "2023-02-02",
                "S01E02",
                JsonArray(),
                "https://example.com/2",
                "2023-02-02T00:00:00Z"
            )
        val episodesDto3 =
            EpisodesDto(
                3,
                "Episode 3",
                "2023-03-03",
                "S01E03",
                JsonArray(),
                "https://example.com/3",
                "2023-03-03T00:00:00Z"
            )

        val expectedEpisodesList = listOf(episodesDto1, episodesDto2, episodesDto3)

        runBlocking {
            `when`(episodesApi.loadEpisodesByIds(itemIds)).thenReturn(expectedEpisodesList)
        }

        val actualEpisodesList = runBlocking { episodesApi.loadEpisodesByIds(itemIds) }

        assertEquals(expectedEpisodesList, actualEpisodesList)
    }
}
