package ru.example.rickandmortyproject.data.locations.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.usecases.LoadLocationsPageUseCase

class LoadLocationsPageUseCaseImpl @Inject constructor(
    private val repository: LocationsRepository
) : LoadLocationsPageUseCase {
    override suspend fun invoke(page: Int): Boolean = repository.loadEpisodesPage(page)
}
