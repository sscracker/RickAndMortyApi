package ru.example.rickandmortyproject.di.module.factory

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.example.rickandmortyproject.presentation.episodes.details.EpisodesDetailsViewModel
import ru.example.rickandmortyproject.utils.DetailsViewModelFactory
import ru.example.rickandmortyproject.utils.ViewModelFactory

@Module
interface ViewModelFactoryModule {
    @Binds
    fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    fun bindEpisodesDetailsViewModelFactory(
        detailsViewModelFactory: DetailsViewModelFactory<EpisodesDetailsViewModel>
    ): ViewModelProvider.Factory
}
