package ru.example.rickandmortyproject.data.locations.usecases

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.domain.locations.list.usecases.GetSingleLocationUseCase

class GetSingleLocationUseCaseImpl @Inject constructor(
    private val repository: LocationsRepository
) : GetSingleLocationUseCase {
    override fun invoke(id: Int): Flow<LocationEntity> = repository.getSingleLocation(id)
}
