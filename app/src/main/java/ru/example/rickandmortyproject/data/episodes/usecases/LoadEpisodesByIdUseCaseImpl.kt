package ru.example.rickandmortyproject.data.episodes.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.usecases.LoadEpisodesByIdUseCase

class LoadEpisodesByIdUseCaseImpl @Inject constructor(
    private val repository: EpisodesRepository
) : LoadEpisodesByIdUseCase {
    override suspend fun invoke(ids: List<Int>): Boolean {
        return repository.loadEpisodesByIds(ids)
    }
}
