package ru.example.rickandmortyproject.domain.episodes.list.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity

interface GetSingleEpisodeUseCase {
    operator fun invoke(episodeId: Int): Flow<EpisodeEntity>
}
