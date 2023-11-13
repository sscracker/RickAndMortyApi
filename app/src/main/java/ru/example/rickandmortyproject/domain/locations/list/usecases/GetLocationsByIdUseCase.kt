package ru.example.rickandmortyproject.domain.locations.list.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity

interface GetLocationsByIdUseCase {
    operator fun invoke(ids: List<Int>): Flow<List<LocationEntity>>
}
