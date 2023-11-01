package ru.example.rickandmortyproject.data.episodes.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings
import ru.example.rickandmortyproject.domain.episodes.list.usecases.GetEpisodeFilterUseCase

class GetEpisodesFilterUseCaseImpl @Inject constructor(
    private val repository: EpisodesRepository
) : GetEpisodeFilterUseCase {
    override suspend fun invoke(): EpisodeFilterSettings = repository.getFilterSettings()
}
