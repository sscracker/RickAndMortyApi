package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class LoadCharactersPageUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : LoadCharactersPageUseCase {
    override suspend fun invoke(pageNumber: Int) = repository.loadCharacterPage(pageNumber)
}