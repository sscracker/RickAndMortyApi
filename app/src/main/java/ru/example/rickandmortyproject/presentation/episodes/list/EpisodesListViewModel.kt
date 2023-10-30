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
): ViewModel() {

    private val _episodesListState = MutableSharedFlow<List<EpisodeEntity>>(1)
    val episodesListState = _episodesListState.asSharedFlow()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow().filterNotNull()

    private var job: Job? = null

    fun onViewCreated(){
        if (pageHolder.currentPageNumber() == INITIAL_EPISODE_PAGE_NUMBER){
            loadEpisodesPage()
        }
        provideEpisodesFlow()
    }

    private fun provideEpisodesFlow() {
        job = viewModelScope.launch(Dispatchers.IO){
            getEpisodesUseCaseImpl.invoke()
                .catch {
                    emitErrorState()
                }
        }
    }

    private fun loadEpisodesPage() {
        viewModelScope.launch(Dispatchers.IO){
            val pageNumber = pageHolder.currentPageNumber()
            val success = loadEpisodesPagesUseCaseImpl.invoke(pageNumber)
            if (success){
                pageHolder.savePageNumber(pageNumber)
            } else{
                emitErrorState()
            }
        }
    }

    private fun emitErrorState(){
        _errorState.tryEmit(Any())
    }

    fun onListEnded(){
        loadEpisodesPage()
    }

    fun onListSwiped(){
      resetData()
    }

    private fun resetData() {
        job?.cancel()
        provideEpisodesFlow()
    }

    companion object{
        private const val INITIAL_EPISODE_PAGE_NUMBER = 1
    }
}