package ru.example.rickandmortyproject.data.locations.usecases

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.domain.locations.list.usecases.GetLocationsUseCases

class GetLocationsUseCaseImpl @Inject constructor(
    private val repository: LocationsRepository
) : GetLocationsUseCases {
    override fun invoke(): Flow<List<LocationEntity>> = repository.getAllLocations()
}
