package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class GetCharacterFilterUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    suspend operator fun invoke() = repository.getFilterSettings()
}