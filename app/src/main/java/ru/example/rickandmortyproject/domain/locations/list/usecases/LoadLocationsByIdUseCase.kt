package ru.example.rickandmortyproject.domain.locations.list.usecases

interface LoadLocationsByIdUseCase {
    suspend operator fun invoke(ids: List<Int>): Boolean
}
