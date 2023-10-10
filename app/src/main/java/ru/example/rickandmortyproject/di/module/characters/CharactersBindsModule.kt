package ru.example.rickandmortyproject.di.module.characters

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.example.rickandmortyproject.data.characters.list.CharactersRepositoryImpl
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.utils.ViewModelFactory
import javax.inject.Singleton

@Module
interface CharactersBindsModule {

    @Binds
    @ActivityScope
    fun bindsCharacterListRepository(
        charactersRepositoryImpl: CharactersRepositoryImpl
    ): CharactersRepository

    @Binds
    fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory
}