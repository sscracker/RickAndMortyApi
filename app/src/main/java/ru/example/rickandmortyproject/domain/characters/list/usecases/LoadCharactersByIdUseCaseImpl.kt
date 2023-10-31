package ru.example.rickandmortyproject.domain.characters.list.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository

class LoadCharactersByIdUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : LoadCharactersByIdUseCase {
    override suspend fun invoke(ids: List<Int>) = repository.loadCharactersById(ids)
}
