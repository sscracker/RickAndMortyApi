package ru.example.rickandmortyproject.data.locations.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.domain.locations.list.usecases.GetLocationsByIdUseCase
import javax.inject.Inject

class GetLocationsByIdUseCaseImpl
    @Inject
    constructor(
        private val repository: LocationsRepository,
    ) : GetLocationsByIdUseCase {
        override fun invoke(ids: List<Int>): Flow<List<LocationEntity>> =
            repository.getLocationsByIds(
                ids,
            )
    }
