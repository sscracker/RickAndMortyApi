package ru.example.rickandmortyproject.presentation.locations.list

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
import ru.example.rickandmortyproject.data.locations.usecases.GetLocationsUseCaseImpl
import ru.example.rickandmortyproject.data.locations.usecases.LoadLocationsPageUseCaseImpl
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.presentation.locations.LocationPageHolder

class LocationsListViewModel @Inject constructor(
    private val getLocationsUseCaseImpl: GetLocationsUseCaseImpl,
    private val loadLocationsPageUseCase: LoadLocationsPageUseCaseImpl,
    private val pageHolder: LocationPageHolder
) : ViewModel() {

    private val _locationsListSharedFlow = MutableSharedFlow<List<LocationEntity>>(1)
    val locationsListStateFlow = _locationsListSharedFlow.asSharedFlow()

    private val _errorStateFlow = MutableStateFlow<Any?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow().filterNotNull()

    private val _emptyStateFlow = MutableStateFlow<Any?>(null)
    val emptyStateFlow = _emptyStateFlow.asStateFlow().filterNotNull()

    private var job: Job? = null

    fun onViewCreated() {
        if (pageHolder.currentPageNumber() == INITIAL_LOCATION_PAGE_NUMBER) {
            loadLocationsPage()
        }
        provideLocationsFlow()
    }

    private fun loadLocationsPage() {
        viewModelScope.launch(Dispatchers.IO) {
            var pageNumber = pageHolder.currentPageNumber()
            val success = loadLocationsPageUseCase.invoke(pageNumber)
            if (success) {
                pageHolder.savePageNumber(pageNumber)
            } else {
                emitErrorState()
            }
        }
    }

    private fun provideLocationsFlow() {
        job = viewModelScope.launch(Dispatchers.IO) {
            getLocationsUseCaseImpl.invoke()
                .catch {
                    emitErrorState()
                }
                .collect { locations ->
                    _locationsListSharedFlow.tryEmit(locations)
                    if (locations.isEmpty()) {
                        emitEmptyResultState()
                    }
                }
        }
    }

    private fun emitErrorState() {
        _errorStateFlow.tryEmit(Any())
    }

    private fun emitEmptyResultState() {
        _emptyStateFlow.tryEmit(Any())
    }

    fun onListSwiped() {
        resetData()
    }

    fun onListEnded() {
        loadLocationsPage()
    }

    private fun resetData() {
        job?.cancel()
        provideLocationsFlow()
    }

    private

    companion object {
        private const val INITIAL_LOCATION_PAGE_NUMBER = 1
    }
}
