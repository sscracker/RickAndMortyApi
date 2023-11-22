package ru.example.rickandmortyproject.presentation.characters.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.domain.characters.details.GetSingleCharacterUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity

private const val TIME_MILLIS = 1000L
class CharacterDetailsViewModel @AssistedInject constructor(
    private val getSingleCharacterUseCaseImpl: GetSingleCharacterUseCaseImpl,
    @Assisted
    private val characterId: Int
) : ViewModel() {

    private val _characterState = MutableStateFlow<CharacterEntity?>(null)
    val characterState = _characterState.asStateFlow()
        .filterNotNull()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private var job: Job? = null

    init {
        provideCharacterFlow(characterId)
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
                    delay(TIME_MILLIS)
                    _characterState.tryEmit(character)
                }
        }
    }

    private fun emitError() {
        _errorState.tryEmit(Any())
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted id: Int): CharacterDetailsViewModel
    }
}
