package ru.example.rickandmortyproject.data.episodes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.example.rickandmortyproject.data.db.lists.EpisodeListDao
import ru.example.rickandmortyproject.data.episodes.api.EpisodesApi
import ru.example.rickandmortyproject.data.episodes.mapper.EpisodesMapper
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity
import ru.example.rickandmortyproject.utils.Preferences
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    preferences: Preferences,
    private val episodesApi: EpisodesApi,
    private val mapper: EpisodesMapper,
    private val episodeListDao: EpisodeListDao
): EpisodesRepository {

    private val preferences = preferences.getEpisodeRepositoryPreferences()
    override fun getAllEpisodes(): Flow<List<EpisodeEntity>> = episodeListDao.getAllEpisodes().map {
        mapper.mapDbModelsListToEntitiesList(it)
    }

    override suspend fun loadEpisodesPage(pageNumber: Int): Boolean {
        return try {
            val page = episodesApi.loadPage(pageNumber)
            episodeListDao.insertList(
                mapper.mapPageToDbModelList(page)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    override fun getEpisode(episodeId: Int) =
        episodeListDao.getSingleEpisode(episodeId).map {
            mapper.mapDbModelToEntity(it)
        }

    override fun getEpisodesByIds(ids: List<Int>) =
        episodeListDao.getEpisodesByIds(ids).map {
            mapper.mapDbModelsListToEntitiesList(it)
        }

    override suspend fun loadEpisodesByIds(ids: List<Int>): Boolean {
        if (ids.isEmpty()){
            return true
        }
        return try {
            val episodes = episodesApi.loadEpisodesByIds(ids.joinToString(","))
            episodeListDao.insertList(
                mapper.mapDtoListToDbModelList(episodes)
            )
            true
        } catch (exception: Throwable){
            false
        }
    }
}