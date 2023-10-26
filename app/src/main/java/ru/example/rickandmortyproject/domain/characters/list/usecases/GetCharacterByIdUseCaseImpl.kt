package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class GetCharacterByIdUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
): GetCharacterByIdUseCase {
   override fun invoke(ids: List<Int>) = repository.getCharactersById(ids)
}