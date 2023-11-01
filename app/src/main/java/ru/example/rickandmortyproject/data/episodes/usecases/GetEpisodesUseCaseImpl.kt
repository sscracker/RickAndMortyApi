package ru.example.rickandmortyproject.data.episodes.usecases

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.domain.episodes.list.usecases.GetEpisodesUseCase

class GetEpisodesUseCaseImpl @Inject constructor(
    private val repository: EpisodesRepository
) : GetEpisodesUseCase {
    override fun invoke(): Flow<List<EpisodeEntity>> {
        return repository.getAllEpisodes()
    }
}
