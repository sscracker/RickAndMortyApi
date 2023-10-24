package ru.example.rickandmortyproject.data.characters.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.usecases.LoadCharactersByIdUseCase
import javax.inject.Inject

class LoadCharactersByIdUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : LoadCharactersByIdUseCase {
    override suspend fun invoke(ids: List<Int>) = repository.loadCharactersById(ids)
}