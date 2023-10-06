package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class LoadCharactersByIdUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    suspend operator fun invoke(ids: List<Int>) = repository.loadCharactersById(ids)
}