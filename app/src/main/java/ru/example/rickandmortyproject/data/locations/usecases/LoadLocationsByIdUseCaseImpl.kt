package ru.example.rickandmortyproject.data.locations.usecases

import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.usecases.LoadLocationsByIdUseCase
import javax.inject.Inject

class LoadLocationsByIdUseCaseImpl
    @Inject
    constructor(
        private val repository: LocationsRepository,
    ) : LoadLocationsByIdUseCase {
        override suspend fun invoke(ids: List<Int>): Boolean = repository.loadLocationsByIds(ids)
    }
