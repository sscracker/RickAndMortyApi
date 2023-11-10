package ru.example.rickandmortyproject.data.locations.mapper

import com.google.gson.Gson
import javax.inject.Inject
import ru.example.rickandmortyproject.data.characters.api.ResponseDto
import ru.example.rickandmortyproject.data.locations.model.LocationDbModel
import ru.example.rickandmortyproject.data.locations.model.LocationsDto
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.utils.getIdListFromUrls

@ActivityScope
class LocationsMapper @Inject constructor() {

    fun mapPageToDbModelList(page: ResponseDto) =
        page.results.mapNotNull { json ->
            Gson().fromJson(json, LocationsDto::class.java)?.let(::mapDtoToDbModel)
        }

    fun mapDtoToDbModel(dto: LocationsDto) = LocationDbModel(
        id = dto.id,
        name = dto.name,
        type = dto.type,
        dimension = dto.dimension,
        residentsId = dto.residents.getIdListFromUrls().joinToString(),
        url = dto.url,
        created = dto.created
    )

    fun mapDtoListToDbModelList(dtoList: List<LocationsDto>) = dtoList.map(::mapDtoToDbModel)

    fun mapDbModelsListToEntitiesList(dbModels: List<LocationDbModel>) = dbModels.map(
        ::mapDbModelToEnity
    )

    fun mapDbModelToEnity(dbModel: LocationDbModel) = LocationEntity(
        id = dbModel.id,
        name = dbModel.name,
        type = dbModel.type,
        dimension = dbModel.dimension,
        residentsId = if (dbModel.residentsId.isNotEmpty()) {
            mapResidentsId(dbModel)
        } else {
            emptyList()
        },
        url = dbModel.url,
        created = dbModel.created
    )

    private fun mapResidentsId(dbModel: LocationDbModel): List<Int> {
        return dbModel.residentsId.split(",").map { it.trim().toInt() }
    }
}
