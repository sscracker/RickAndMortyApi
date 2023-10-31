package ru.example.rickandmortyproject.domain.characters.list.usecases

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity

interface GetCharactersUseCase {
    operator fun invoke(): Flow<List<CharacterEntity>>
}
