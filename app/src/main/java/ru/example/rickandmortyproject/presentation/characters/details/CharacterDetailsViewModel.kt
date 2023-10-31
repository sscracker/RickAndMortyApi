package ru.example.rickandmortyproject.presentation.characters.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.domain.characters.details.GetSingleCharacterUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity

class CharacterDetailsViewModel @Inject constructor(
    private val getSingleCharacterUseCaseImpl: GetSingleCharacterUseCaseImpl
) : ViewModel() {

    private val _characterState = MutableStateFlow<CharacterEntity?>(null)
    val characterState = _characterState.asStateFlow()
        .filterNotNull()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private var job: Job? = null

    fun onViewCreated(id: Int) {
        provideCharacterFlow(id)
    }

    fun onButtonReloadPressed(id: Int) {
        job?.cancel()
        provideCharacterFlow(id)
    }

    private fun provideCharacterFlow(id: Int) {
        job = viewModelScope.launch(Dispatchers.IO) {
            getSingleCharacterUseCaseImpl.invoke(id)
                .catch {
                    emitError()
                }
                .collect { character ->
                    _characterState.tryEmit(character)
                }
        }
    }

    private fun emitError() {
        _errorState.tryEmit(Any())
    }
}
