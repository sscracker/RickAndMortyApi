package ru.example.rickandmortyproject.data.episodes.usecases

import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.usecases.LoadEpisodesByIdUseCase
import javax.inject.Inject

class LoadEpisodesByIdUseCaseImpl
    @Inject
    constructor(
        private val repository: EpisodesRepository,
    ) : LoadEpisodesByIdUseCase {
        override suspend fun invoke(ids: List<Int>): Boolean {
            return repository.loadEpisodesByIds(ids)
        }
    }
