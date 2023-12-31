package ru.example.rickandmortyproject.data.episodes.usecases

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.domain.episodes.list.usecases.GetSingleEpisodeUseCase

class GetSingleEpisodeUseCaseImpl @Inject constructor(
    private val repository: EpisodesRepository
) : GetSingleEpisodeUseCase {
    override fun invoke(episodeId: Int): Flow<EpisodeEntity> = repository.getEpisode(episodeId)
}
