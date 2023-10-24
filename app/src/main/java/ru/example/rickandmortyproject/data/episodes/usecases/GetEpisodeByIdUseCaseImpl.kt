package ru.example.rickandmortyproject.data.episodes.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity
import ru.example.rickandmortyproject.domain.episodes.usecases.GetEpisodeByUdUseCase
import javax.inject.Inject

class GetEpisodeByIdUseCaseImpl @Inject constructor(
    private val repository: EpisodesRepository
) : GetEpisodeByUdUseCase {
    override fun invoke(ids: List<Int>): Flow<List<EpisodeEntity>> {
        return repository.getEpisodesByIds(ids)
    }
}