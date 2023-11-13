package ru.example.rickandmortyproject.domain.locations.list.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity

interface GetSingleLocationUseCase {
    operator fun invoke(id: Int): Flow<LocationEntity>
}
