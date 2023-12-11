package ru.example.rickandmortyproject.domain.locations

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings

interface LocationsRepository {
    fun getAllLocations(): Flow<List<LocationEntity>>

    suspend fun loadEpisodesPage(page: Int): Boolean

    fun getSingleLocation(locationId: Int): Flow<LocationEntity>

    fun getLocationsByIds(ids: List<Int>): Flow<List<LocationEntity>>

    suspend fun loadLocationsByIds(ids: List<Int>): Boolean

    suspend fun getLocationsFilterSettings(): LocationFilterSettings

    suspend fun saveLocationsFilterSettings(settings: LocationFilterSettings): Boolean
}
