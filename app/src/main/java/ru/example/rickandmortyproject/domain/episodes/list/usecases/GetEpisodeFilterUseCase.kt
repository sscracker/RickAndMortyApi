package ru.example.rickandmortyproject.domain.episodes.list.usecases

import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings

interface GetEpisodeFilterUseCase {
    suspend operator fun invoke(): EpisodeFilterSettings
}
