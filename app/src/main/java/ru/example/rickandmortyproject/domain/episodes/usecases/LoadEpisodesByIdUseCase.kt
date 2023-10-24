package ru.example.rickandmortyproject.domain.episodes.usecases

interface LoadEpisodesByIdUseCase {
    suspend operator fun invoke(ids: List<Int>): Boolean
}