package ru.example.rickandmortyproject.data.episodes.mapper

import com.google.gson.Gson
import ru.example.rickandmortyproject.data.characters.api.ResponseDto
import ru.example.rickandmortyproject.data.episodes.model.EpisodeDbModel
import ru.example.rickandmortyproject.data.episodes.model.EpisodesDto
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity
import ru.example.rickandmortyproject.utils.getIdListFromUrls
import javax.inject.Inject

@ActivityScope
class EpisodesMapper @Inject constructor() {

    fun mapPageToDbModelList(page: ResponseDto) =
        page.results.mapNotNull { json ->
            Gson().fromJson(json, EpisodesDto::class.java)?.let(::mapDtoToDbModel)
        }

    fun mapDtoListToDbModelList(dtoList: List<EpisodesDto>) = dtoList.map(::mapDtoToDbModel)

    fun mapDtoToDbModel(dto: EpisodesDto) = EpisodeDbModel(
        id = dto.id,
        name = dto.name,
        airDate = dto.airDate,
        episode = dto.episode,
        characterId = dto.characters.getIdListFromUrls().joinToString(),
        url = dto.url,
        created = dto.created
    )

    fun mapDbModelsListToEntitiesList(dbModels: List<EpisodeDbModel>) =
        dbModels.map(::mapDbModelToEntity)

    fun mapDbModelToEntity(dbModel: EpisodeDbModel) = EpisodeEntity(
        id = dbModel.id,
        name = dbModel.name,
        airDate = dbModel.airDate,
        episode = dbModel.episode,
        charactersId = if (dbModel.characterId.isNotEmpty()) {
            mapCharactersId(dbModel)
        } else {
            emptyList()
        },
        url = dbModel.url,
        created = dbModel.created
    )

    private fun mapCharactersId(dbModel: EpisodeDbModel): List<Int> {
        return dbModel.characterId.split(",").map { it.trim().toInt() }
    }
}