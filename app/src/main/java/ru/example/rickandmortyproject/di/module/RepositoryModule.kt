package ru.example.rickandmortyproject.di.module

import dagger.Binds
import dagger.Module
import ru.example.rickandmortyproject.data.characters.list.CharactersRepositoryImpl
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository

@Module
interface RepositoryModule {

    @Binds
    @ActivityScope
    fun bindsCharacterListRepository(
        charactersRepositoryImpl: CharactersRepositoryImpl
    ): CharactersRepository
}