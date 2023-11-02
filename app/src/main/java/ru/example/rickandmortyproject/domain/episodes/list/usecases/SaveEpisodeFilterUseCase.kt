package ru.example.rickandmortyproject.domain.episodes.list.usecases

import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings

interface SaveEpisodeFilterUseCase {
    suspend operator fun invoke(settings: EpisodeFilterSettings): Boolean
}
