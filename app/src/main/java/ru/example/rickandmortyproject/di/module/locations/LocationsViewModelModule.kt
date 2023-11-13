package ru.example.rickandmortyproject.di.module.locations

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.example.rickandmortyproject.di.key.ViewModelKey
import ru.example.rickandmortyproject.presentation.locations.list.LocationsListViewModel

@Module
interface LocationsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LocationsListViewModel::class)
    fun bindLocationsListViewModel(locationsListViewModel: LocationsListViewModel): ViewModel
}
