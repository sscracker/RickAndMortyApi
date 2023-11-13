package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class LoadCharactersByIdUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : LoadCharactersByIdUseCase {
    override suspend fun invoke(ids: List<Int>) = repository.loadCharactersById(ids)
}