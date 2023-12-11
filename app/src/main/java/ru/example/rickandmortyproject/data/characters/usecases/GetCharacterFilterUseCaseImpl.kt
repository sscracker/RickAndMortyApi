package ru.example.rickandmortyproject.data.characters.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.usecases.GetCharacterFilterUseCase
import javax.inject.Inject

class GetCharacterFilterUseCaseImpl
    @Inject
    constructor(
        private val repository: CharactersRepository,
    ) : GetCharacterFilterUseCase {
        override suspend fun invoke() = repository.getFilterSettings()
    }
