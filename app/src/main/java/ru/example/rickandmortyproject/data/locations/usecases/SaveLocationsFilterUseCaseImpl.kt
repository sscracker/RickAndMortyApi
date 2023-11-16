package ru.example.rickandmortyproject.data.locations.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings
import ru.example.rickandmortyproject.domain.locations.list.usecases.SaveLocationsFilterUseCase

class SaveLocationsFilterUseCaseImpl @Inject constructor(
    private val repository: LocationsRepository
) : SaveLocationsFilterUseCase {
    override suspend fun invoke(settings: LocationFilterSettings): Boolean =
        repository.saveLocationsFilterSettings(
            settings
        )
}
