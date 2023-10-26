package ru.example.rickandmortyproject.domain.characters.list.usecases

import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import javax.inject.Inject

class GetCharacterFilterUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharacterFilterUseCase {
    override suspend fun invoke() = repository.getFilterSettings()
}