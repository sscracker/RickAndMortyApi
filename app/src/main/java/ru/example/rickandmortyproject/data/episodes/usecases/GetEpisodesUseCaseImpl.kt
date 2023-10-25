package ru.example.rickandmortyproject.data.episodes.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity
import ru.example.rickandmortyproject.domain.episodes.usecases.GetEpisodesUseCase
import javax.inject.Inject

class GetEpisodesUseCaseImpl @Inject constructor(
    private val repository: EpisodesRepository
) : GetEpisodesUseCase {
    override fun invoke(ids: List<Int>): Flow<List<EpisodeEntity>> {
        return repository.getEpisodesByIds(ids)
    }
}