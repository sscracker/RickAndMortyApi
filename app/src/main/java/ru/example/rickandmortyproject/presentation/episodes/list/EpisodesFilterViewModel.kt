package ru.example.rickandmortyproject.presentation.episodes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.data.episodes.usecases.GetEpisodesFilterUseCaseImpl
import ru.example.rickandmortyproject.data.episodes.usecases.SaveEpisodesFilterUseCaseImpl
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings

class EpisodesFilterViewModel @Inject constructor(
    private val getEpisodesFilterUseCase: GetEpisodesFilterUseCaseImpl,
    private val saveEpisodeFilterUseCase: SaveEpisodesFilterUseCaseImpl
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = getEpisodesFilterUseCase.invoke()
            _getEpisodesFilterStateFlow.tryEmit(settings)
        }
    }

    private val _getEpisodesFilterStateFlow = MutableStateFlow<EpisodeFilterSettings?>(null)
    val getEpisodesFilterStateFlow = _getEpisodesFilterStateFlow.asStateFlow()
        .filterNotNull()

    private val _saveFilterStateFlow = MutableStateFlow<Unit?>(null)
    val saveFilterStateFlow = _saveFilterStateFlow.asStateFlow()
        .filterNotNull()

    fun onApplyClick(settings: EpisodeFilterSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = saveEpisodeFilterUseCase.invoke(settings)
            if (success) {
                _saveFilterStateFlow.tryEmit(Unit)
            }
        }
    }
}
