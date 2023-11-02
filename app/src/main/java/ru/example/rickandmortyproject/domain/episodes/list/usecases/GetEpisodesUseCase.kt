package ru.example.rickandmortyproject.domain.episodes.list.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity

interface GetEpisodesUseCase {
    operator fun invoke(): Flow<List<EpisodeEntity>>
}
