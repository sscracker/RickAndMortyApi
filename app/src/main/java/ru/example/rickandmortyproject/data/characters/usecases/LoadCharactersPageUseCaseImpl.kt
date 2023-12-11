package ru.example.rickandmortyproject.data.characters.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.usecases.LoadCharactersPageUseCase
import javax.inject.Inject

class LoadCharactersPageUseCaseImpl
    @Inject
    constructor(
        private val repository: CharactersRepository,
    ) : LoadCharactersPageUseCase {
        override suspend fun invoke(pageNumber: Int) = repository.loadCharacterPage(pageNumber)
    }
