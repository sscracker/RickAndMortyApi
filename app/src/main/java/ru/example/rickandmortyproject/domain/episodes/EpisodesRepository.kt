package ru.example.rickandmortyproject.domain.episodes

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings

interface EpisodesRepository {
    fun getAllEpisodes(): Flow<List<EpisodeEntity>>

    suspend fun loadEpisodesPage(pageNumber: Int): Boolean

    fun getEpisode(episodeId: Int): Flow<EpisodeEntity>

    fun getEpisodesByIds(ids: List<Int>): Flow<List<EpisodeEntity>>

    suspend fun loadEpisodesByIds(ids: List<Int>): Boolean

    suspend fun getFilterSettings(): EpisodeFilterSettings

    suspend fun saveFilterSettings(settings: EpisodeFilterSettings): Boolean
}
