package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class LoadCharactersPageUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    suspend operator fun invoke(pageNumber: Int) = repository.loadCharacterPage(pageNumber)
}