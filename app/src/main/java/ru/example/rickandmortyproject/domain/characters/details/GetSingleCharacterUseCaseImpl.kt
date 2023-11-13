package ru.example.rickandmortyproject.domain.characters.details

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class GetSingleCharacterUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetSingleCharacterUseCase {
    override fun invoke(id: Int) = repository.getCharacter(id)
}