package ru.example.rickandmortyproject.presentation.locations.list

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
import ru.example.rickandmortyproject.data.locations.usecases.GetLocationsFilterUseCaseImpl
import ru.example.rickandmortyproject.data.locations.usecases.GetLocationsUseCaseImpl
import ru.example.rickandmortyproject.data.locations.usecases.LoadLocationsPageUseCaseImpl
import ru.example.rickandmortyproject.data.locations.usecases.SaveLocationsFilterUseCaseImpl
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings
import javax.inject.Inject

class LocationsListViewModel
    @Inject
    constructor(
        private val getLocationsUseCaseImpl: GetLocationsUseCaseImpl,
        private val loadLocationsPageUseCaseImpl: LoadLocationsPageUseCaseImpl,
        private val pageHolder: LocationsPageHolder,
        private val matcher: LocationsMatcher,
        private val getLocationsFilterUseCaseImpl: GetLocationsFilterUseCaseImpl,
        private val saveLocationsFilterUseCaseImpl: SaveLocationsFilterUseCaseImpl,
    ) : ViewModel() {
        private val _locationsListSharedFlow = MutableSharedFlow<List<LocationEntity>>(1)
        val locationsListSharedFlow = _locationsListSharedFlow.asSharedFlow()

        private val _errorStateFlow = MutableStateFlow<Any?>(null)
        val errorStateFlow = _errorStateFlow.asStateFlow().filterNotNull()

        private val _notEmptyFilterStateFlow = MutableStateFlow<Boolean?>(null)
        val notEmptyFilterStateFlow = _notEmptyFilterStateFlow.asStateFlow().filterNotNull()

        private val emptyFilterSettings = LocationFilterSettings()

        private var searchQuery = ""

        private var job: Job? = null

        fun onViewCreated() {
            if (pageHolder.currentPageNumber() == INITIAL_PAGE_NUMBER) {
                loadLocationsPage()
            }
            provideLocationsFlow()
            loadLocationFilters()
        }

        private fun loadLocationFilters() {
            viewModelScope.launch(Dispatchers.IO) {
                val settings = getLocationsFilterUseCaseImpl.invoke()
                if (settings != emptyFilterSettings) {
                    _notEmptyFilterStateFlow.tryEmit(true)
                } else {
                    _notEmptyFilterStateFlow.tryEmit(false)
                }
            }
        }

        private fun provideLocationsFlow() {
            job =
                viewModelScope.launch(Dispatchers.IO) {
                    getLocationsUseCaseImpl.invoke()
                        .catch {
                            emitErrorState()
                        }
                        .collect { locations ->
                            val filter = getLocationsFilterUseCaseImpl.invoke()
                            val filtered = locations.filter { matcher.isLocationMatches(filter, it) }
                            emitFilteredWithQuery(filtered)
                        }
                }
        }

        private fun emitFilteredWithQuery(locationsList: List<LocationEntity>) {
            if (searchQuery.isNotEmpty()) {
                locationsList.filter { locations ->
                    locations.name.contains(searchQuery, true)
                }.also { locations ->
                    _locationsListSharedFlow.tryEmit(locations)
                }
            } else {
                _locationsListSharedFlow.tryEmit(locationsList)
            }
        }

        private fun loadLocationsPage() {
            viewModelScope.launch(Dispatchers.IO) {
                var pageNumber = pageHolder.currentPageNumber()
                val success = loadLocationsPageUseCaseImpl.invoke(pageNumber)
                if (success) {
                    pageNumber++
                    pageHolder.savePageNumber(pageNumber)
                } else {
                    emitErrorState()
                }
            }
        }

        fun onFilterClearButtonClick() {
            viewModelScope.launch(Dispatchers.IO) {
                val emptySettings = saveLocationsFilterUseCaseImpl.invoke(emptyFilterSettings)
                if (emptySettings) {
                    resetData()
                    _notEmptyFilterStateFlow.tryEmit(false)
                }
            }
        }

        fun onSearchQueryChanged(query: String?) {
            searchQuery = query?.trim().orEmpty()
            resetData()
        }

        fun onFilterSettingsChanged() {
            resetData()
        }

        private fun emitErrorState() {
            _errorStateFlow.tryEmit(Any())
        }

        fun onListEnded() {
            loadLocationsPage()
        }

        fun onListSwiped() {
            resetData()
        }

        private fun resetData() {
            job?.cancel()
            provideLocationsFlow()
        }

        companion object {
            private const val INITIAL_PAGE_NUMBER = 1
        }
    }
