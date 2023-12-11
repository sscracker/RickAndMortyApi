package ru.example.rickandmortyproject.data.episodes

import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import ru.example.rickandmortyproject.data.db.lists.EpisodeListDao
import ru.example.rickandmortyproject.data.episodes.api.EpisodesApi
import ru.example.rickandmortyproject.data.episodes.mapper.EpisodesMapper
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings
import ru.example.rickandmortyproject.utils.Preferences
import javax.inject.Inject

class EpisodesRepositoryImpl
    @Inject
    constructor(
        preferences: Preferences,
        private val episodesApi: EpisodesApi,
        private val mapper: EpisodesMapper,
        private val episodeListDao: EpisodeListDao,
    ) : EpisodesRepository {
        private val preferences = preferences.getEpisodeRepositoryPreferences()

        override fun getAllEpisodes() =
            episodeListDao.getAllEpisodes().map(
                mapper::mapDbModelsListToEntitiesList,
            )

        override suspend fun loadEpisodesPage(pageNumber: Int): Boolean {
            kotlin.runCatching {
                val page = episodesApi.loadPage(pageNumber)
                episodeListDao.insertList(
                    mapper.mapPageToDbModelList(page),
                )
            }.fold(
                onSuccess = { return true },
                onFailure = { return false },
            )
        }

        override fun getEpisode(episodeId: Int) =
            episodeListDao.getSingleEpisode(episodeId).map(
                mapper::mapDbModelToEntity,
            )

        override fun getEpisodesByIds(ids: List<Int>) =
            episodeListDao.getEpisodesByIds(ids).map(
                mapper::mapDbModelsListToEntitiesList,
            )

        override suspend fun loadEpisodesByIds(ids: List<Int>): Boolean {
            if (ids.isEmpty()) {
                return true
            }
            kotlin.runCatching {
                val episodes = episodesApi.loadEpisodesByIds(ids.joinToString(","))
                episodeListDao.insertList(
                    mapper.mapDtoListToDbModelList(episodes),
                )
            }.fold(
                onSuccess = { return true },
                onFailure = { return false },
            )
        }

        override suspend fun getFilterSettings(): EpisodeFilterSettings {
            val json = preferences.getString(KEY_EPISODES_FILTER, null)
            return json?.let {
                Gson().fromJson(json, EpisodeFilterSettings::class.java)
            } ?: EpisodeFilterSettings()
        }

        override suspend fun saveFilterSettings(settings: EpisodeFilterSettings): Boolean {
            val json = Gson().toJson(settings)
            preferences.edit().putString(KEY_EPISODES_FILTER, json).apply()
            return true
        }

        companion object {
            private const val KEY_EPISODES_FILTER = "episodesFilter"
        }
    }
