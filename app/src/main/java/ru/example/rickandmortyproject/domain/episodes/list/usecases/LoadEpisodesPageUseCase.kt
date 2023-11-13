package ru.example.rickandmortyproject.domain.episodes.list.usecases

interface LoadEpisodesPageUseCase {
    suspend operator fun invoke(pageNumber: Int): Boolean
}
