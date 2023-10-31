package ru.example.rickandmortyproject.domain.characters.details

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository

class GetSingleCharacterUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetSingleCharacterUseCase {
    override fun invoke(id: Int) = repository.getCharacter(id)
}
