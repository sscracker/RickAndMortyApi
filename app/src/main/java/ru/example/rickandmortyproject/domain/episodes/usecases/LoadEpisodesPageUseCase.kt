package ru.example.rickandmortyproject.domain.episodes.usecases

interface LoadEpisodesPageUseCase {
    suspend operator fun invoke(pageNumber: Int): Boolean
}