import com.google.gson.JsonArray
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.example.rickandmortyproject.data.episodes.mapper.EpisodesMapper
import ru.example.rickandmortyproject.data.episodes.model.EpisodeDbModel
import ru.example.rickandmortyproject.data.episodes.model.EpisodesDto

class EpisodesMapperTest {

    private lateinit var episodesMapper: EpisodesMapper

    @Before
    fun setUp() {
        episodesMapper = EpisodesMapper()
    }

    @Test
    fun `test mapping EpisodesDto to EpisodeDbModel`() {
        val episodesDto = EpisodesDto(
            id = 1,
            name = "Test Episode",
            airDate = "2023-12-11",
            episode = "S01E01",
            characters = JsonArray(),
            url = "https://episode/1",
            created = "2023-12-10"
        )

        val episodeDbModel = episodesMapper.mapDtoToDbModel(episodesDto)

        assertEquals(episodesDto.id, episodeDbModel.id)
        assertEquals(episodesDto.name, episodeDbModel.name)
        assertEquals(episodesDto.airDate, episodeDbModel.airDate)
        assertEquals(episodesDto.episode, episodeDbModel.episode)
        assertEquals(episodesDto.characters.joinToString(), episodeDbModel.characterId)
        assertEquals(episodesDto.url, episodeDbModel.url)
        assertEquals(episodesDto.created, episodeDbModel.created)
    }

    @Test
    fun `test mapping EpisodeDbModel to EpisodeEntity`() {
        val episodeDbModel = EpisodeDbModel(
            id = 1,
            name = "Test Episode",
            airDate = "2023-12-11",
            episode = "S01E01",
            characterId = "1, 2, 3",
            url = "https://episode/1",
            created = "2023-12-10"
        )

        val episodeEntity = episodesMapper.mapDbModelToEntity(episodeDbModel)

        assertEquals(episodeDbModel.id, episodeEntity.id)
        assertEquals(episodeDbModel.name, episodeEntity.name)
        assertEquals(episodeDbModel.airDate, episodeEntity.airDate)
        assertEquals(episodeDbModel.episode, episodeEntity.episodeCode)
        assertEquals(listOf(1, 2, 3), episodeEntity.charactersId)
        assertEquals(episodeDbModel.url, episodeEntity.url)
        assertEquals(episodeDbModel.created, episodeEntity.created)
    }
}
