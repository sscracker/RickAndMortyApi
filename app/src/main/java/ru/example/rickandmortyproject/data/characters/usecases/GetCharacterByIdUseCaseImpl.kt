package ru.example.rickandmortyproject.data.characters.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.domain.characters.list.usecases.GetCharacterByIdUseCase

class GetCharacterByIdUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharacterByIdUseCase {
    override fun invoke(ids: List<Int>) = repository.getCharactersById(ids)
}
