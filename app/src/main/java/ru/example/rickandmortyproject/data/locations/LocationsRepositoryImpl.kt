package ru.example.rickandmortyproject.data.locations

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.example.rickandmortyproject.data.db.lists.LocationsListDao
import ru.example.rickandmortyproject.data.locations.api.LocationsApi
import ru.example.rickandmortyproject.data.locations.mapper.LocationsMapper
import ru.example.rickandmortyproject.domain.locations.LocationsRepository
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.utils.Preferences

class LocationsRepositoryImpl @Inject constructor(
    preferences: Preferences,
    private val locationsApi: LocationsApi,
    private val mapper: LocationsMapper,
    private val locationsListDao: LocationsListDao
) : LocationsRepository {

    private val preferences = preferences.getLocationRepositoryPreferences()
    override fun getAllLocations(): Flow<List<LocationEntity>> =
        locationsListDao.getAllLocations().map(mapper::mapDbModelsListToEntitiesList)

    override suspend fun loadEpisodesPage(page: Int): Boolean {
        kotlin.runCatching {
            val locationPage = locationsApi.loadPage(page)
            locationsListDao.insertList(
                mapper.mapPageToDbModelList(locationPage)
            )
        }.fold(
            onSuccess = { return true },
            onFailure = { return false }
        )
    }

    override fun getSingleLocation(locationId: Int): Flow<LocationEntity> =
        locationsListDao.getSingleLocation(locationId).map(mapper::mapDbModelToEnity)

    override fun getLocationsByIds(ids: List<Int>): Flow<List<LocationEntity>> =
        locationsListDao.getLocationsByIds(ids).map(mapper::mapDbModelsListToEntitiesList)

    override suspend fun loadLocationsByIds(ids: List<Int>): Boolean {
        if (ids.isEmpty()) {
            return true
        }
        kotlin.runCatching {
            val locations = locationsApi.loadLocationsByIds(ids.joinToString(","))
            locationsListDao.insertList(
                mapper.mapDtoListToDbModelList(locations)
            )
        }.fold(
            onSuccess = { return true },
            onFailure = { return false }
        )
    }
}
