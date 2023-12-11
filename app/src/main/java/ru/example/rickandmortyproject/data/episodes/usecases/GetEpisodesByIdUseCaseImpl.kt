package ru.example.rickandmortyproject.data.episodes.usecases

import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.usecases.GetEpisodesByUdUseCase
import javax.inject.Inject

class GetEpisodesByIdUseCaseImpl
    @Inject
    constructor(
        private val repository: EpisodesRepository,
    ) : GetEpisodesByUdUseCase {
        override fun invoke(ids: List<Int>) = repository.getEpisodesByIds(ids)
    }
