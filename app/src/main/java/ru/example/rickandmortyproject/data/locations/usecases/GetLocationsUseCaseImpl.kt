package ru.example.rickandmortyproject.data.locations.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.domain.locations.list.usecases.GetLocationsUseCases
import javax.inject.Inject

class GetLocationsUseCaseImpl
    @Inject
    constructor(
        private val repository: LocationsRepository,
    ) : GetLocationsUseCases {
        override fun invoke(): Flow<List<LocationEntity>> = repository.getAllLocations()
    }
