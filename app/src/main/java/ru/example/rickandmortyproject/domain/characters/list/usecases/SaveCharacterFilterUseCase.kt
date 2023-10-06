package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings
import javax.inject.Inject

class SaveCharacterFilterUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    suspend operator fun invoke(settings: CharacterFilterSettings) =
        repository.saveFilterSettings(settings)
}