package ru.example.rickandmortyproject.domain.locations.list.usecases

import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings

interface GetLocationFiltersUseCase {
    suspend operator fun invoke(): LocationFilterSettings
}
