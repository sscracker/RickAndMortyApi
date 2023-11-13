package ru.example.rickandmortyproject.domain.episodes.list.usecases

interface LoadEpisodesByIdUseCase {
    suspend operator fun invoke(ids: List<Int>): Boolean
}
