package ru.example.rickandmortyproject.presentation.locations.details

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
import ru.example.rickandmortyproject.data.characters.usecases.GetCharacterByIdUseCaseImpl
import ru.example.rickandmortyproject.data.characters.usecases.LoadCharactersByIdUseCaseImpl
import ru.example.rickandmortyproject.data.locations.usecases.GetSingleLocationUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity

class LocationDetailsViewModel
    @AssistedInject
    constructor(
        private val getSingleLocationUseCaseImpl: GetSingleLocationUseCaseImpl,
        private val getCharacterByIdUseCaseImpl: GetCharacterByIdUseCaseImpl,
        private val loadCharactersByIdUseCaseImpl: LoadCharactersByIdUseCaseImpl,
        @Assisted
        private val locationId: Int,
    ) : ViewModel() {
        private val _locationsStateFlow = MutableStateFlow<LocationEntity?>(null)
        val locationsStateFlow = _locationsStateFlow.asStateFlow().filterNotNull()

        private val _errorStateFlow = MutableStateFlow<Any?>(null)
        val errorStateFlow = _errorStateFlow.asStateFlow().filterNotNull()

        private val _residentsListStateFlow = MutableStateFlow<List<CharacterEntity>?>(null)
        val residentsListStateFlow = _residentsListStateFlow.asStateFlow().filterNotNull()

        private var locationJob: Job? = null

        private var residentsJob: Job? = null

        private var location: LocationEntity? = null

        private var loadedCount = COUNT_START

        init {
            provideLocationFlow(locationId)
        }

        fun onButtonReloadClick(locationId: Int) {
            locationJob?.cancel()
            residentsJob?.cancel()
            provideLocationFlow(locationId)
        }

        fun saveLocation(locationEntity: LocationEntity) {
            location = locationEntity
        }

        private fun provideLocationFlow(locationId: Int) {
            locationJob =
                viewModelScope.launch(Dispatchers.IO) {
                    getSingleLocationUseCaseImpl.invoke(locationId)
                        .catch {
                            emitErrorState()
                        }
                        .collect {
                            delay(TIME_MILLIS)
                            _locationsStateFlow.tryEmit(it)
                            provideLocationDetails(it)
                            loading()
                        }
                }
        }

        private fun provideLocationDetails(locationEntity: LocationEntity) {
            loadResidents(locationEntity.residentsId)
            provideResidentsFlow(locationEntity.residentsId)
        }

        private fun loadResidents(ids: List<Int>) {
            viewModelScope.launch(Dispatchers.IO) {
                loadCharactersByIdUseCaseImpl.invoke(ids)
            }
        }

        private fun provideResidentsFlow(ids: List<Int>) {
            if (ids.isEmpty()) {
                _residentsListStateFlow.tryEmit(emptyList())
                return
            }
            residentsJob =
                viewModelScope.launch(Dispatchers.IO) {
                    getCharacterByIdUseCaseImpl.invoke(ids)
                        .catch {
                            emitErrorState()
                        }
                        .collect {
                            _residentsListStateFlow.tryEmit(it)
                            loading()
                        }
                }
        }

        private fun emitErrorState() {
            _errorStateFlow.tryEmit(Any())
        }

        private fun loading(): Boolean {
            loadedCount++
            return loadedCount == COUNT_EXPECTED
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted id: Int,
            ): LocationDetailsViewModel
        }

        companion object {
            private const val COUNT_START = 0
            private const val COUNT_EXPECTED = 2
            private const val TIME_MILLIS = 1000L
        }
    }
