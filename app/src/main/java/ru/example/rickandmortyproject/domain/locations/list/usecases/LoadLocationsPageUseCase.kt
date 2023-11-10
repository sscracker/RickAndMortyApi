package ru.example.rickandmortyproject.domain.locations.list.usecases

interface LoadLocationsPageUseCase {
    suspend operator fun invoke(page: Int): Boolean
}
