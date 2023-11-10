package ru.example.rickandmortyproject.di.module.locations

import dagger.Binds
import dagger.Module
import ru.example.rickandmortyproject.data.locations.LocationsRepositoryImpl
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.locations.LocationsRepository

@Module
interface LocationsBindsModule {

    @Binds
    @ActivityScope
    fun bindLocationsRepository(
        locationsRepositoryImpl: LocationsRepositoryImpl
    ): LocationsRepository
}
