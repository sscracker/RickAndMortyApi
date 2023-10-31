package ru.example.rickandmortyproject.domain.episodes.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity

interface GetEpisodesUseCase {
    operator fun invoke(): Flow<List<EpisodeEntity>>
}
