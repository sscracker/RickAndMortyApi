package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings

interface SaveCharacterFilterUseCase {
    suspend operator fun invoke(settings: CharacterFilterSettings): Boolean
}
