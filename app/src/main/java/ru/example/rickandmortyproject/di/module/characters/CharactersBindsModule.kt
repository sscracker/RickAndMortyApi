package ru.example.rickandmortyproject.di.module.characters

import dagger.Binds
import dagger.Module
import ru.example.rickandmortyproject.data.characters.CharactersRepositoryImpl
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository

@Module
interface CharactersBindsModule {
    @Binds
    @ActivityScope
    fun bindsCharacterListRepository(charactersRepositoryImpl: CharactersRepositoryImpl): CharactersRepository
}
