package ru.example.rickandmortyproject.data.episodes.usecases

import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.usecases.LoadEpisodesPageUseCase
import javax.inject.Inject

class LoadEpisodesPagesUseCaseImpl
    @Inject
    constructor(
        private val repository: EpisodesRepository,
    ) : LoadEpisodesPageUseCase {
        override suspend fun invoke(pageNumber: Int): Boolean {
            return repository.loadEpisodesPage(pageNumber)
        }
    }
