package ru.example.rickandmortyproject.presentation.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings
import ru.example.rickandmortyproject.domain.characters.list.usecases.GetCharacterFilterUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.usecases.SaveCharacterFilterUseCaseImpl
import javax.inject.Inject

class CharactersFiltersViewModel @Inject constructor(
    private val getCharactersFilterUseCase: GetCharacterFilterUseCaseImpl,
    private val saveCharactersFilterUseCase: SaveCharacterFilterUseCaseImpl
) : ViewModel() {

    private val _charactersFilterState = MutableStateFlow<CharacterFilterSettings?>(null)
    val charactersFilterState = _charactersFilterState.asStateFlow()
        .filterNotNull()

    private val _filterSavedState = MutableStateFlow<Unit?>(null)
    val filterSavedState = _filterSavedState.asStateFlow()
        .filterNotNull()

    fun onViewCreated() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = getCharactersFilterUseCase.invoke()
            _charactersFilterState.tryEmit(settings)
        }
    }

    fun onApplyPressed(settings: CharacterFilterSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = saveCharactersFilterUseCase.invoke(settings)
            if (success) {
                _filterSavedState.tryEmit(Unit)
            }
        }
    }
}
