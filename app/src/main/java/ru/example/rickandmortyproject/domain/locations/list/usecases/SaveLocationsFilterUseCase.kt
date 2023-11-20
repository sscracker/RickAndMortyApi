package ru.example.rickandmortyproject.domain.locations.list.usecases

import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings

interface SaveLocationsFilterUseCase {
    suspend operator fun invoke(settings: LocationFilterSettings): Boolean
}
