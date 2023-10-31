package ru.example.rickandmortyproject.domain.characters.list.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity

interface GetCharacterByIdUseCase {
    operator fun invoke(ids: List<Int>): Flow<List<CharacterEntity>>
}
