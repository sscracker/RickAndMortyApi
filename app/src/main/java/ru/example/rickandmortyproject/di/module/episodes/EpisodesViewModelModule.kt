package ru.example.rickandmortyproject.di.module.episodes

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.example.rickandmortyproject.di.key.ViewModelKey
import ru.example.rickandmortyproject.presentation.episodes.list.EpisodesFilterViewModel
import ru.example.rickandmortyproject.presentation.episodes.list.EpisodesListViewModel

@Module
interface EpisodesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EpisodesListViewModel::class)
    fun bindEpisodesListViewModel(episodesListViewModel: EpisodesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesFilterViewModel::class)
    fun bindEpisodesFilterViewModel(episodesFilterViewModel: EpisodesFilterViewModel): ViewModel
}
