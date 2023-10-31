package ru.example.rickandmortyproject.domain.characters.list.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings

class SaveCharacterFilterUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : SaveCharacterFilterUseCase {
    override suspend fun invoke(settings: CharacterFilterSettings) =
        repository.saveFilterSettings(settings)
}
