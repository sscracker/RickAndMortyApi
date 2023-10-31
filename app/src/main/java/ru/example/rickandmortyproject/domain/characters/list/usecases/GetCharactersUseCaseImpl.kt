package ru.example.rickandmortyproject.domain.characters.list.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository

class GetCharactersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharactersUseCase {
    override fun invoke() = repository.getAllCharacters()
}
