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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.data.episodes.usecases.GetEpisodesByIdUseCaseImpl
import ru.example.rickandmortyproject.data.episodes.usecases.LoadEpisodesByIdUseCaseImpl
import ru.example.rickandmortyproject.data.locations.usecases.GetSingleLocationUseCaseImpl
import ru.example.rickandmortyproject.data.locations.usecases.LoadLocationsByIdUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.details.GetSingleCharacterUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity

private const val TIME_MILLIS = 1000L

class CharacterDetailsViewModel @AssistedInject constructor(
    private val getSingleCharacterUseCaseImpl: GetSingleCharacterUseCaseImpl,
    private val getEpisodesByIdUseCaseImpl: GetEpisodesByIdUseCaseImpl,
    private val loadLocationsByIdUseCaseImpl: LoadLocationsByIdUseCaseImpl,
    private val loadEpisodesByIdUseCaseImpl: LoadEpisodesByIdUseCaseImpl,
    private val getSingleLocationUseCaseImpl: GetSingleLocationUseCaseImpl,
    @Assisted
    private val characterId: Int
) : ViewModel() {

    private val _characterStateFlow = MutableStateFlow<CharacterEntity?>(null)
    val characterStateFlow = _characterStateFlow.asStateFlow()
        .filterNotNull()

    private val _originStateFlow = MutableStateFlow<String?>(null)
    val originStateFlow = _originStateFlow.asStateFlow().filterNotNull()

    private val _locationStateFlow = MutableStateFlow<String?>(null)
    val locationStateFlow = _locationStateFlow.asStateFlow().filterNotNull()

    private val _episodesListStateFlow = MutableStateFlow<List<EpisodeEntity>?>(null)
    val episodesListStateFlow = _episodesListStateFlow.asStateFlow().filterNotNull()

    private val _errorStateFlow = MutableStateFlow<Any?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()
        .filterNotNull()

    private var characterJob: Job? = null
    private var originJob: Job? = null
    private var locationJob: Job? = null
    private var episodesListJob: Job? = null

    private var loadedCount = COUNT_START

    init {
        provideCharacterFlow(characterId)
    }

    fun onButtonReloadPressed(id: Int) {
        characterJob?.cancel()
        originJob?.cancel()
        locationJob?.cancel()
        episodesListJob?.cancel()
        provideCharacterFlow(id)
    }

    private fun provideCharacterFlow(id: Int) {
        characterJob = viewModelScope.launch(Dispatchers.IO) {
            getSingleCharacterUseCaseImpl.invoke(id)
                .catch {
                    emitError()
                }
                .collect { character ->
                    delay(TIME_MILLIS)
                    _characterStateFlow.tryEmit(character)
                    loading()
                    provideExtraCharacterDetails(character)
                }
        }
    }

    private fun provideExtraCharacterDetails(entity: CharacterEntity) {
        loadLocationAndOrigin(entity)
        loadEpisodes(entity.episodesId)
        provideOriginFlow(entity.originId)
        provideLocationFlow(entity.locationId)
        provideEpisodesListFlow(entity.episodesId)
    }

    private fun loadLocationAndOrigin(entity: CharacterEntity) {
        val ids = mutableListOf<Int>()
        if (entity.originId != -1) {
            ids.add(entity.originId)
        }
        if (entity.locationId != -1) {
            ids.add(entity.locationId)
        }
        viewModelScope.launch(Dispatchers.IO) {
            loadLocationsByIdUseCaseImpl.invoke(ids.toList())
        }
    }

    private fun loadEpisodes(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            loadEpisodesByIdUseCaseImpl.invoke(ids)
        }
    }

    private fun provideOriginFlow(id: Int) {
        if (id == -1) {
            _originStateFlow.tryEmit(UNKNOWN_VALUE)
            return
        }
        originJob = viewModelScope.launch(Dispatchers.IO) {
            getSingleLocationUseCaseImpl.invoke(id)
                .catch {
                    emitError()
                }
                .collect { origin ->
                    _originStateFlow.tryEmit(origin.name)
                }
        }
    }

    private fun provideLocationFlow(id: Int) {
        if (id == -1) {
            _locationStateFlow.tryEmit(UNKNOWN_VALUE)
            return
        }
        locationJob = viewModelScope.launch(Dispatchers.IO) {
            getSingleLocationUseCaseImpl.invoke(id)
                .catch {
                    emitError()
                }
                .collect { location ->
                    _locationStateFlow.tryEmit(location.name)
                }
        }
    }

    private fun provideEpisodesListFlow(ids: List<Int>) {
        if (ids.isEmpty()) {
            _episodesListStateFlow.tryEmit(emptyList())
            return
        }
        episodesListJob = viewModelScope.launch(Dispatchers.IO) {
            getEpisodesByIdUseCaseImpl.invoke(ids)
                .catch {
                    emitError()
                }
                .collect { episodesList ->
                    _episodesListStateFlow.tryEmit(episodesList)
                }
        }
    }

    private fun loading(): Boolean {
        loadedCount++
        return loadedCount == COUNT_EXPECTED
    }

    private fun emitError() {
        _errorStateFlow.tryEmit(Any())
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted id: Int): CharacterDetailsViewModel
    }

    companion object {
        private const val COUNT_START = 0
        private const val COUNT_EXPECTED = 2
        private const val UNKNOWN_VALUE = "Unknown"
    }
}
