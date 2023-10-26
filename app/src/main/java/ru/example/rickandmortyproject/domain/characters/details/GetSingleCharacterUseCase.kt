package ru.example.rickandmortyproject.domain.characters.details

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity

interface GetSingleCharacterUseCase {
    operator fun invoke(id: Int): Flow<CharacterEntity>
}