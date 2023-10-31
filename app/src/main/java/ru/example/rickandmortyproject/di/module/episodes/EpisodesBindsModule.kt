package ru.example.rickandmortyproject.di.module.episodes

import dagger.Binds
import dagger.Module
import ru.example.rickandmortyproject.data.episodes.EpisodesRepositoryImpl
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.episodes.EpisodesRepository

@Module
interface EpisodesBindsModule {

    @Binds
    @ActivityScope
    fun bindsEpisodesListRepository(
        episodesRepositoryImpl: EpisodesRepositoryImpl
    ): EpisodesRepository
}
