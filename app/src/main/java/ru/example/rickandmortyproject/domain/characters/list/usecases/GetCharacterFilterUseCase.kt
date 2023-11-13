package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings

interface GetCharacterFilterUseCase {
    suspend operator fun invoke(): CharacterFilterSettings
}
