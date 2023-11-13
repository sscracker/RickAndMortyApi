package ru.example.rickandmortyproject.domain.episodes.list.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity

interface GetEpisodesByUdUseCase {
    operator fun invoke(ids: List<Int>): Flow<List<EpisodeEntity>>
}
