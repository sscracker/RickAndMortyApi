package ru.example.rickandmortyproject.domain.characters.list.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository

class LoadCharactersPageUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : LoadCharactersPageUseCase {
    override suspend fun invoke(pageNumber: Int) = repository.loadCharacterPage(pageNumber)
}
