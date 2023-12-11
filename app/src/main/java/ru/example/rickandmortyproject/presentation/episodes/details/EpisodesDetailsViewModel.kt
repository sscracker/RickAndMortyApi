package ru.example.rickandmortyproject.presentation.episodes.details

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
import ru.example.rickandmortyproject.data.characters.usecases.GetCharacterByIdUseCaseImpl
import ru.example.rickandmortyproject.data.characters.usecases.LoadCharactersByIdUseCaseImpl
import ru.example.rickandmortyproject.data.episodes.usecases.GetSingleEpisodeUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity

class EpisodesDetailsViewModel
    @AssistedInject
    constructor(
        private val getSingleEpisodeUseCase: GetSingleEpisodeUseCaseImpl,
        private val getCharacterByIdUseCaseImpl: GetCharacterByIdUseCaseImpl,
        private val loadCharactersByIdUseCaseImpl: LoadCharactersByIdUseCaseImpl,
        @Assisted private val episodeId: Int,
    ) : ViewModel() {
        private val _episodeStateFlow = MutableStateFlow<EpisodeEntity?>(null)
        val episodeStateFlow = _episodeStateFlow.asStateFlow().filterNotNull()

        private val _charactersListStateFlow = MutableStateFlow<List<CharacterEntity>?>(null)
        val charactersListStateFlow = _charactersListStateFlow.asStateFlow().filterNotNull()

        private val _errorStateFlow = MutableStateFlow<Any?>(null)
        val errorStateFlow = _errorStateFlow.asStateFlow().filterNotNull()

        private var episodeJob: Job? = null
        private var charactersJob: Job? = null

        private var loadedCount = COUNT_START

        private var episode: EpisodeEntity? = null

        init {
            provideEpisodeFlow(episodeId)
        }

        fun onButtonReloadClick(episodeId: Int) {
            episodeJob?.cancel()
            charactersJob?.cancel()
            provideEpisodeFlow(episodeId)
        }

        private fun loading(): Boolean {
            loadedCount++
            return loadedCount == COUNT_EXPECTED
        }

        private fun provideEpisodeFlow(id: Int) {
            episodeJob =
                viewModelScope.launch(Dispatchers.IO) {
                    getSingleEpisodeUseCase.invoke(id)
                        .catch {
                            emitError()
                        }
                        .collect {
                            delay(1000)
                            _episodeStateFlow.tryEmit(it)
                            provideEpisodeDetails(it)
                            loading()
                        }
                }
        }

        fun saveEpisode(episodeEntity: EpisodeEntity) {
            episode = episodeEntity
        }

        private fun provideEpisodeDetails(episodeEntity: EpisodeEntity) {
            loadCharacters(episodeEntity.charactersId)
            provideCharactersFlow(episodeEntity.charactersId)
        }

        private fun loadCharacters(ids: List<Int>) {
            viewModelScope.launch(Dispatchers.IO) {
                loadCharactersByIdUseCaseImpl.invoke(ids)
            }
        }

        private fun provideCharactersFlow(ids: List<Int>) {
            if (ids.isEmpty()) {
                _charactersListStateFlow.tryEmit(emptyList())
                return
            }
            charactersJob =
                viewModelScope.launch(Dispatchers.IO) {
                    getCharacterByIdUseCaseImpl.invoke(ids)
                        .catch {
                            emitError()
                        }
                        .collect {
                            _charactersListStateFlow.tryEmit(it)
                            loading()
                        }
                }
        }

        private fun emitError() {
            _errorStateFlow.tryEmit(Any())
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted id: Int,
            ): EpisodesDetailsViewModel
        }

        companion object {
            private const val COUNT_START = 0
            private const val COUNT_EXPECTED = 2
        }
    }
