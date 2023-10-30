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
import ru.example.rickandmortyproject.data.episodes.usecases.GetEpisodesUseCaseImpl
import ru.example.rickandmortyproject.data.episodes.usecases.LoadEpisodesPagesUseCaseImpl
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity
import javax.inject.Inject

class EpisodesListViewModel @Inject constructor(
    private val getEpisodesUseCaseImpl: GetEpisodesUseCaseImpl,
    private val loadEpisodesPagesUseCaseImpl: LoadEpisodesPagesUseCaseImpl,
    private val pageHolder: EpisodesPageHolder
) : ViewModel() {

    private val _episodesListStateFlow = MutableSharedFlow<List<EpisodeEntity>>(1)
    val episodesListStateFlow = _episodesListStateFlow.asSharedFlow()

    private val _errorStateFlow = MutableStateFlow<Unit?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow().filterNotNull()

    private val _emptyStateFlow = MutableStateFlow<Unit?>(null)
    val emptyStateFlow = _emptyStateFlow.asStateFlow().filterNotNull()

    private var job: Job? = null

    fun onViewCreated() {
        if (pageHolder.currentPageNumber() == INITIAL_EPISODE_PAGE_NUMBER) {
            loadEpisodesPage()
        }
        provideEpisodesFlow()
    }

    private fun provideEpisodesFlow() {
        job = viewModelScope.launch(Dispatchers.IO) {
            getEpisodesUseCaseImpl.invoke()
                .catch {
                    emitErrorState()
                }
                .collect { episodesList ->
                    emitEpisodeListState(episodesList)
                }
        }
    }

    private fun emitEpisodeListState(episodes: List<EpisodeEntity>) {
        _episodesListStateFlow.tryEmit(episodes)
        if (episodes.isEmpty()) {
            emitEmptyResultState()
        }
    }

    private fun loadEpisodesPage() {
        viewModelScope.launch(Dispatchers.IO) {
            val pageNumber = pageHolder.currentPageNumber()
            val success = loadEpisodesPagesUseCaseImpl.invoke(pageNumber)
            if (success) {
                pageHolder.savePageNumber(pageNumber)
            } else {
                emitErrorState()
            }
        }
    }

    private fun emitErrorState() {
        _errorStateFlow.tryEmit(Unit)
    }

    private fun emitEmptyResultState() {
        _emptyStateFlow.tryEmit(Unit)
    }

    fun onListEnded() {
        loadEpisodesPage()
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