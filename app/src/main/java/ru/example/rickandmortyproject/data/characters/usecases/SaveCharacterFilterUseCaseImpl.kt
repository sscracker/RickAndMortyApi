package ru.example.rickandmortyproject.data.characters.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings
import ru.example.rickandmortyproject.domain.characters.list.usecases.SaveCharacterFilterUseCase
import javax.inject.Inject

class SaveCharacterFilterUseCaseImpl
    @Inject
    constructor(
        private val repository: CharactersRepository,
    ) : SaveCharacterFilterUseCase {
        override suspend fun invoke(settings: CharacterFilterSettings) =
            repository.saveFilterSettings(
                settings,
            )
    }
