package ru.example.rickandmortyproject.presentation.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.domain.characters.list.CharactersListUseCase
import ru.example.rickandmortyproject.presentation.characters.list.mapper.SingleCharacterDomainToSingleCharacterUiMapper
import ru.example.rickandmortyproject.presentation.characters.list.model.SingleCharacter
import ru.example.rickandmortyproject.utils.Connectivity
import ru.example.rickandmortyproject.utils.Response
import ru.example.rickandmortyproject.utils.ViewState
import javax.inject.Inject

class CharacterListViewModel @Inject constructor(
    private val charactersListUseCase: CharactersListUseCase,
    private val mapper: SingleCharacterDomainToSingleCharacterUiMapper,
    private val connectivity: Connectivity
) : ViewModel() {

    private var characters = MutableStateFlow<ViewState<List<SingleCharacter>>>(ViewState.Loading)

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        if (connectivity.isNetworkAvailable()) {
            loadAllCharacters()
        } else {
            characters.value = ViewState.Error(Throwable("Отсутствует интернет соединение"))
            loadAllCharactersFromLocal()
        }
    }

    fun loadAllCharactersFromLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            charactersListUseCase.getAllCharactersFromLocal().collect() { data ->
                characters.value = ViewState.Data(mapper.map(data))
            }
        }
    }

    fun loadAllCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            charactersListUseCase.getAllCharacters().collect { response ->
                characters.value = when (response) {
                    is Response.Success -> ViewState.Data(mapper.map(response.data))
                    is Response.Failure -> ViewState.Error(response.error)
                }
            }
        }
    }

    fun getAllCharacters(): MutableStateFlow<ViewState<List<SingleCharacter>>> = characters
}