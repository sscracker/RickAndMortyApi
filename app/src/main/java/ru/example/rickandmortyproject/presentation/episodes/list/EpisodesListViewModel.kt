package ru.example.rickandmortyproject.presentation.episodes.list

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
import ru.example.rickandmortyproject.data.episodes.usecases.GetEpisodesFilterUseCaseImpl
import ru.example.rickandmortyproject.data.episodes.usecases.GetEpisodesUseCaseImpl
import ru.example.rickandmortyproject.data.episodes.usecases.LoadEpisodesPagesUseCaseImpl
import ru.example.rickandmortyproject.data.episodes.usecases.SaveEpisodesFilterUseCaseImpl
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings
import javax.inject.Inject

class EpisodesListViewModel
    @Inject
    constructor(
        private val getEpisodesFilterUseCaseImpl: GetEpisodesFilterUseCaseImpl,
        private val saveEpisodesFilterUseCaseImpl: SaveEpisodesFilterUseCaseImpl,
        private val getEpisodesUseCaseImpl: GetEpisodesUseCaseImpl,
        private val loadEpisodesPagesUseCaseImpl: LoadEpisodesPagesUseCaseImpl,
        private val pageHolder: EpisodesPageHolder,
        private val matcher: EpisodesMatcher,
    ) : ViewModel() {
        private val _episodesListStateFlow = MutableSharedFlow<List<EpisodeEntity>>(1)
        val episodesListStateFlow = _episodesListStateFlow.asSharedFlow()

        private val _errorStateFlow = MutableStateFlow<Any?>(null)
        val errorStateFlow = _errorStateFlow.asStateFlow().filterNotNull()

        private val _notEmptyFiltersStateFlow = MutableStateFlow<Boolean?>(null)
        val notEmptyFilterStateFlow =
            _notEmptyFiltersStateFlow.asStateFlow()
                .filterNotNull()

        private val _emptyStateFlow = MutableStateFlow<Any?>(null)
        val emptyStateFlow = _emptyStateFlow.asStateFlow().filterNotNull()

        private var job: Job? = null

        private val emptyFilterSettings = EpisodeFilterSettings()

        private var searchQuery = ""

        fun onViewCreated() {
            if (pageHolder.currentPageNumber() == INITIAL_EPISODE_PAGE_NUMBER) {
                loadEpisodesPage()
            }
            provideEpisodesFlow()
            loadFilters()
        }

        private fun loadFilters() {
            viewModelScope.launch(Dispatchers.IO) {
                val filterSettings = getEpisodesFilterUseCaseImpl.invoke()
                if (filterSettings != emptyFilterSettings) {
                    _notEmptyFiltersStateFlow.tryEmit(true)
                } else {
                    _notEmptyFiltersStateFlow.tryEmit(false)
                }
            }
        }

        private fun provideEpisodesFlow() {
            job =
                viewModelScope.launch(Dispatchers.IO) {
                    getEpisodesUseCaseImpl.invoke()
                        .catch {
                            emitErrorState()
                        }
                        .collect { episodesList ->
                            val filter = getEpisodesFilterUseCaseImpl.invoke()
                            val filtered =
                                episodesList.filter {
                                    matcher.isEpisodeMatches(
                                        filter,
                                        it,
                                    )
                                }
                            emitFilteredWithQuery(filtered)
                        }
                }
        }

        private fun emitFilteredWithQuery(episodesList: List<EpisodeEntity>) {
            if (searchQuery.isNotEmpty()) {
                episodesList.filter { episodes ->
                    episodes.name.contains(searchQuery, true)
                }.also { episodes ->
                    _episodesListStateFlow.tryEmit(episodes)
                    if (episodes.isEmpty()) {
                        emitEmptyResultState()
                    }
                }
            } else {
                _episodesListStateFlow.tryEmit(episodesList)
                if (episodesList.isEmpty()) {
                    emitEmptyResultState()
                }
            }
        }

        private fun loadEpisodesPage() {
            viewModelScope.launch(Dispatchers.IO) {
                var pageNumber = pageHolder.currentPageNumber()
                val success = loadEpisodesPagesUseCaseImpl.invoke(pageNumber)
                if (success) {
                    pageNumber++
                    pageHolder.savePageNumber(pageNumber)
                } else {
                    emitErrorState()
                }
            }
        }

        private fun emitErrorState() {
            _errorStateFlow.tryEmit(Any())
        }

        private fun emitEmptyResultState() {
            _emptyStateFlow.tryEmit(Any())
        }

        fun onListEnded() {
            loadEpisodesPage()
        }

        fun onFilterSettingsChanged() {
            resetData()
        }

        fun onButtonClearPressed() {
            viewModelScope.launch(Dispatchers.IO) {
                val emptySettingsSaved = saveEpisodesFilterUseCaseImpl.invoke(emptyFilterSettings)
                if (emptySettingsSaved) {
                    resetData()
                    _notEmptyFiltersStateFlow.tryEmit(false)
                }
            }
        }

        fun onSearchQueryChanged(query: String?) {
            searchQuery = query?.trim().orEmpty()
            resetData()
        }

        fun onListSwiped() {
            resetData()
        }

        private fun resetData() {
            job?.cancel()
            provideEpisodesFlow()
        }

        companion object {
            private const val INITIAL_EPISODE_PAGE_NUMBER = 1
        }
    }
