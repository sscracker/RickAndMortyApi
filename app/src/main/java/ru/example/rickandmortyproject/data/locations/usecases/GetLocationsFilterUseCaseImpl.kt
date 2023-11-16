package ru.example.rickandmortyproject.data.locations.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings
import ru.example.rickandmortyproject.domain.locations.list.usecases.GetLocationFiltersUseCase

class GetLocationsFilterUseCaseImpl @Inject constructor(
    private val repository: LocationsRepository
) : GetLocationFiltersUseCase {
    override suspend fun invoke(): LocationFilterSettings = repository.getLocationsFilterSettings()
}
