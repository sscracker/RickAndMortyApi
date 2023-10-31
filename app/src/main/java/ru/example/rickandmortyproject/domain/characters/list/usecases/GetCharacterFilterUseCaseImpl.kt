package ru.example.rickandmortyproject.domain.characters.list.usecases

import javax.inject.Inject
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository

class GetCharacterFilterUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharacterFilterUseCase {
    override suspend fun invoke() = repository.getFilterSettings()
}
