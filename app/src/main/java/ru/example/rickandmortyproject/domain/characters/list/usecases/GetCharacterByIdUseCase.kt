package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    operator fun invoke(ids: List<Int>) = repository.getCharactersById(ids)
}