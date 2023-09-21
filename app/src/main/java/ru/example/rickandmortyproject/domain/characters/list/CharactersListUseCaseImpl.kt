package ru.example.rickandmortyproject.domain.characters.list

import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.domain.characters.list.model.SingleCharacterDomain
import ru.example.rickandmortyproject.utils.Response
import javax.inject.Inject

class CharactersListUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
): CharactersListUseCase {
    override fun getAllCharacters(): Flow<Response<List<SingleCharacterDomain>>> {
        return charactersRepository.getAllCharacters()
    }

    override fun getAllCharactersFromLocal(): Flow<List<SingleCharacterDomain>> {
        return charactersRepository.getAllCharactersFromLocal()
    }
}