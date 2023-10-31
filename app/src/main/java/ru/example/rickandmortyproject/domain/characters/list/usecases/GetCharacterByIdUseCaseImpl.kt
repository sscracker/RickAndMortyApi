package ru.example.rickandmortyproject.domain.characters.list.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository

class GetCharacterByIdUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharacterByIdUseCase {
    override fun invoke(ids: List<Int>) = repository.getCharactersById(ids)
}
