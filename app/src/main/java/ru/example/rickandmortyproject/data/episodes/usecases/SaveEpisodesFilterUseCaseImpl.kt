package ru.example.rickandmortyproject.data.episodes.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings
import ru.example.rickandmortyproject.domain.episodes.list.usecases.SaveEpisodeFilterUseCase

class SaveEpisodesFilterUseCaseImpl @Inject constructor(
    private val repository: EpisodesRepository
) : SaveEpisodeFilterUseCase {
    override suspend fun invoke(settings: EpisodeFilterSettings) = repository.saveFilterSettings(
        settings
    )
}
