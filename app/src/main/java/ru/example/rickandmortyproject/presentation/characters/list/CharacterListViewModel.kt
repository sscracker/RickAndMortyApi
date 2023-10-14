package ru.example.rickandmortyproject.presentation.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.characters.list.usecases.GetCharacterFilterUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.usecases.GetCharactersUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.usecases.LoadCharactersPageUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.usecases.SaveCharacterFilterUseCaseImpl
import javax.inject.Inject

class CharacterListViewModel @Inject constructor(
    private val getCharacterFilterUseCase: GetCharacterFilterUseCaseImpl,
    private val saveCharacterFilterUseCaseImpl: SaveCharacterFilterUseCaseImpl,
    private val getCharactersUseCaseImpl: GetCharactersUseCaseImpl,
    private val loadCharactersPageUseCaseImpl: LoadCharactersPageUseCaseImpl,
    private val pageHolder: CharactersPageHolder,
    private val matcher: CharactersMatcher
) : ViewModel() {

    private val _charactersListState = MutableSharedFlow<List<CharacterEntity>>(1)
    val charactersListState = _charactersListState.asSharedFlow()

    private val _notEmptyFilterState = MutableStateFlow<Boolean?>(null)
    val notEmptyFilterState = _notEmptyFilterState.asStateFlow()
        .filterNotNull()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private val _emptyResultState = MutableStateFlow<Any?>(null)
    val emptyResultState = _emptyResultState.asStateFlow()
        .filterNotNull()

    private var job: Job? = null
    private val emptyFilterSettings = CharacterFilterSettings(
        EMPTY_STRING, null, EMPTY_STRING, EMPTY_STRING, null
    )

    private var searchQuery = EMPTY_STRING

    fun onViewCreated() {
        if (pageHolder.currentPageNumber() == INITIAL_PAGE_NUMBER) {
            loadPage()
        }
        provideCharactersFlow()
        loadFilterIsPresent()
    }

    private fun loadFilterIsPresent() {
        viewModelScope.launch(Dispatchers.IO) {
            val filterSettings = getCharacterFilterUseCase.invoke()
            if (filterSettings != emptyFilterSettings) {
                _notEmptyFilterState.tryEmit(true)
            } else {
                _notEmptyFilterState.tryEmit(false)
            }
        }
    }

    private fun provideCharactersFlow() {
        job = viewModelScope.launch(Dispatchers.IO) {
            getCharactersUseCaseImpl.invoke()
                .catch {
                    emitErrorState()
                }
                .collect { charactersList ->
                    val filter = getCharacterFilterUseCase.invoke()
                    val filtered = charactersList.filter { matcher.isCharacterMatches(filter, it) }
                    emitFilteredWithQuery(filtered)
                }
        }
    }

    private fun emitFilteredWithQuery(charactersList: List<CharacterEntity>) {
        if (searchQuery != EMPTY_STRING) {
            charactersList.filter { characters ->
                characters.name.contains(searchQuery, true)
            }.also { characters ->
                _charactersListState.tryEmit(characters)
                if (characters.isEmpty()) {
                    emitEmptyResultState()
                }
            }
        } else {
            _charactersListState.tryEmit(charactersList)
            if (charactersList.isEmpty()) {
                emitEmptyResultState()
            }
        }
    }

    private fun loadPage() {
        viewModelScope.launch(Dispatchers.IO) {
            var pageNumber = pageHolder.currentPageNumber()
            val success = loadCharactersPageUseCaseImpl.invoke(pageNumber)
            if (success) {
                pageHolder.savePageNumber(pageNumber)
            } else {
                emitErrorState()
            }
        }
    }

    fun onListEnded() {
        loadPage()
    }

    fun onListSwiped() {
        resetData()
    }

    fun onFilterSettingsChanged() {
        resetData()
    }

    fun onButtonClearPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            val emptySettingsSaved = saveCharacterFilterUseCaseImpl.invoke(emptyFilterSettings)
            if (emptySettingsSaved) {
                resetData()
                _notEmptyFilterState.tryEmit(false)
            }
        }
    }

    fun onSearchQueryChanged(query: String?) {
        searchQuery = query?.trim() ?: EMPTY_STRING
        resetData()
    }


    private fun emitErrorState() {
        _errorState.tryEmit(Any())
    }

    private fun emitEmptyResultState() {
        _emptyResultState.tryEmit(Any())
    }

    private fun resetData() {
        job?.cancel()
        provideCharactersFlow()
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val INITIAL_PAGE_NUMBER = 1
    }
}