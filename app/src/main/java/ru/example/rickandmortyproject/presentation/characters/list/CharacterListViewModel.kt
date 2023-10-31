package ru.example.rickandmortyproject.presentation.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.data.characters.usecases.GetCharacterFilterUseCaseImpl
import ru.example.rickandmortyproject.data.characters.usecases.GetCharactersUseCaseImpl
import ru.example.rickandmortyproject.data.characters.usecases.LoadCharactersPageUseCaseImpl
import ru.example.rickandmortyproject.data.characters.usecases.SaveCharacterFilterUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings

class CharacterListViewModel @Inject constructor(
    private val getCharacterFilterUseCase: GetCharacterFilterUseCaseImpl,
    private val saveCharacterFilterUseCaseImpl: SaveCharacterFilterUseCaseImpl,
    private val getCharactersUseCaseImpl: GetCharactersUseCaseImpl,
    private val loadCharactersPageUseCaseImpl: LoadCharactersPageUseCaseImpl,
    private val pageHolder: CharactersPageHolder,
    private val matcher: CharactersMatcher
) : ViewModel() {

    private val _charactersListStateFlow = MutableSharedFlow<List<CharacterEntity>>(1)
    val charactersListStateFlow = _charactersListStateFlow.asSharedFlow()

    private val _notEmptyFilterStateFlow = MutableStateFlow<Boolean?>(null)
    val notEmptyFilterStateFlow = _notEmptyFilterStateFlow.asStateFlow()
        .filterNotNull()

    private val _errorStateFlow = MutableStateFlow<Any?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()
        .filterNotNull()

    private val _emptyResultStateFLow = MutableStateFlow<Any?>(null)
    val emptyResultStateFLow = _emptyResultStateFLow.asStateFlow()
        .filterNotNull()

    private var job: Job? = null
    private val emptyFilterSettings = CharacterFilterSettings(
        EMPTY_STRING,
        null,
        EMPTY_STRING,
        EMPTY_STRING,
        null
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
                _notEmptyFilterStateFlow.tryEmit(true)
            } else {
                _notEmptyFilterStateFlow.tryEmit(false)
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
                _charactersListStateFlow.tryEmit(characters)
                if (characters.isEmpty()) {
                    emitEmptyResultState()
                }
            }
        } else {
            _charactersListStateFlow.tryEmit(charactersList)
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
                pageNumber++
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
                _notEmptyFilterStateFlow.tryEmit(false)
            }
        }
    }

    fun onSearchQueryChanged(query: String?) {
        searchQuery = query?.trim() ?: EMPTY_STRING
        resetData()
    }

    private fun emitErrorState() {
        _errorStateFlow.tryEmit(Any())
    }

    private fun emitEmptyResultState() {
        _emptyResultStateFLow.tryEmit(Any())
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
