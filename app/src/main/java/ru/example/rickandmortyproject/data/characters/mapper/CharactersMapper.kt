package ru.example.rickandmortyproject.data.characters.mapper

import com.google.gson.Gson
import ru.example.rickandmortyproject.data.characters.model.CharacterDto
import ru.example.rickandmortyproject.data.characters.api.ResponseDto
import ru.example.rickandmortyproject.data.characters.model.CharacterDbModel
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterGender
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterStatus
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import javax.inject.Inject

const val ALIVE = "Alive"
const val DEAD = "Dead"
const val UNKNOWN = "unknown"
const val FEMALE = "Female"
const val MALE = "Male"
const val GENDERLESS = "Genderless"

@ActivityScope
class CharactersMapper @Inject constructor() {

    fun mapPageToDbModelList(page: ResponseDto) =
        page.results.mapNotNull { json ->
            Gson().fromJson(json, CharacterDto::class.java)?.let(::mapDtoToDbModel)
        }

    fun mapDtoListToDbModelList(dtoList: List<CharacterDto>) = dtoList.map { mapDtoToDbModel(it) }

    private fun mapDtoToDbModel(dto: CharacterDto) = CharacterDbModel(
        id = dto.id,
        name = dto.name,
        status = when (dto.status) {
            ALIVE -> CharacterStatus.ALIVE.ordinal
            DEAD -> CharacterStatus.DEAD.ordinal
            UNKNOWN -> CharacterStatus.UNKNOWN.ordinal
            else -> throw RuntimeException("Wrong status value: ${dto.status}")
        },
        species = dto.species,
        type = dto.type,
        gender = when (dto.gender) {
            FEMALE -> CharacterGender.FEMALE.ordinal
            MALE -> CharacterGender.MALE.ordinal
            GENDERLESS -> CharacterGender.GENDERLESS.ordinal
            UNKNOWN -> CharacterGender.UNKNOWN.ordinal
            else -> throw RuntimeException("Wrong gender value: ${dto.gender}")
        },
        image = dto.image,
        url = dto.url,
        created = dto.created
    )

    fun mapDbModelsListToEntitiesList(dbModels: List<CharacterDbModel>) = dbModels.map {
        mapDbModelToDomain(it)
    }

    fun mapDbModelToDomain(dbModel: CharacterDbModel) = CharacterEntity(
        id = dbModel.id,
        name = dbModel.name,
        status = when (dbModel.status) {
            CharacterStatus.ALIVE.ordinal -> CharacterStatus.ALIVE
            CharacterStatus.DEAD.ordinal -> CharacterStatus.DEAD
            CharacterStatus.UNKNOWN.ordinal -> CharacterStatus.UNKNOWN
            else -> throw RuntimeException("Wrong status value: ${dbModel.status}")
        },
        species = dbModel.species,
        type = dbModel.type,
        gender = when (dbModel.gender) {
            CharacterGender.FEMALE.ordinal -> CharacterGender.FEMALE
            CharacterGender.MALE.ordinal -> CharacterGender.MALE
            CharacterGender.GENDERLESS.ordinal -> CharacterGender.GENDERLESS
            CharacterGender.UNKNOWN.ordinal -> CharacterGender.UNKNOWN
            else -> throw RuntimeException("Wrong gender value: ${dbModel.gender}")
        },
        image = dbModel.image,
        url = dbModel.url,
        created = dbModel.created
    )
}