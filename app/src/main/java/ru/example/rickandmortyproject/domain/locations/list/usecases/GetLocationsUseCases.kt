package ru.example.rickandmortyproject.domain.locations.list.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity

interface GetLocationsUseCases {
    operator fun invoke(): Flow<List<LocationEntity>>
}
