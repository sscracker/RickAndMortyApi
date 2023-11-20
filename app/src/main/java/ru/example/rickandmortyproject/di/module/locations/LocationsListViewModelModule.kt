package ru.example.rickandmortyproject.di.module.locations

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.example.rickandmortyproject.di.key.ViewModelKey
import ru.example.rickandmortyproject.presentation.locations.list.LocationsFilterViewModel
import ru.example.rickandmortyproject.presentation.locations.list.LocationsListViewModel

@Module
interface LocationsListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LocationsListViewModel::class)
    fun bindLocationListViewModel(locationsListViewModel: LocationsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsFilterViewModel::class)
    fun bindLocationFilterViewModel(locationsFilterViewModel: LocationsFilterViewModel): ViewModel
}
