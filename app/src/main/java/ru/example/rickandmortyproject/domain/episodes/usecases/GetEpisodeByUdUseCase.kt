package ru.example.rickandmortyproject.domain.episodes.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity

interface GetEpisodeByUdUseCase {
    operator fun invoke(ids: List<Int>): Flow<List<EpisodeEntity>>
}