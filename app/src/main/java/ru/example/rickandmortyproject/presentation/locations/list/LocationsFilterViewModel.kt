package ru.example.rickandmortyproject.presentation.locations.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.data.locations.usecases.GetLocationsFilterUseCaseImpl
import ru.example.rickandmortyproject.data.locations.usecases.SaveLocationsFilterUseCaseImpl
import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings
import javax.inject.Inject

class LocationsFilterViewModel
    @Inject
    constructor(
        private val getLocationsFilterUseCaseImpl: GetLocationsFilterUseCaseImpl,
        private val saveLocationsFilterUseCaseImpl: SaveLocationsFilterUseCaseImpl,
    ) : ViewModel() {
        private val _getLocationsFilterStateFlow = MutableStateFlow<LocationFilterSettings?>(null)
        val getLocationsFilterStateFlow = _getLocationsFilterStateFlow.asStateFlow().filterNotNull()

        private val _saveFilterStateFlow = MutableStateFlow<Unit?>(null)
        val saveLocationsFilterStateFlow = _saveFilterStateFlow.asStateFlow().filterNotNull()

        init {
            viewModelScope.launch(Dispatchers.IO) {
                val settings = getLocationsFilterUseCaseImpl.invoke()
                _getLocationsFilterStateFlow.tryEmit(settings)
            }
        }

        fun onApplyClick(settings: LocationFilterSettings) {
            viewModelScope.launch(Dispatchers.IO) {
                val success = saveLocationsFilterUseCaseImpl.invoke(settings)
                if (success) {
                    _saveFilterStateFlow.tryEmit(Unit)
                }
            }
        }
    }
