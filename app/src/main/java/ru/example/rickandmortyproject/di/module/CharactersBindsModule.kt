package ru.example.rickandmortyproject.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.example.rickandmortyproject.data.characters.list.CharactersRepositoryImpl
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.characters.list.CharactersListUseCase
import ru.example.rickandmortyproject.domain.characters.list.CharactersListUseCaseImpl
import ru.example.rickandmortyproject.domain.characters.list.CharactersRepository
import ru.example.rickandmortyproject.utils.ViewModelFactory

@Module
interface CharactersBindsModule {

    @Binds
    @ActivityScope
    fun bindsCharacterListRepository(
        charactersRepositoryImpl: CharactersRepositoryImpl
    ): CharactersRepository

    @Binds
    @ActivityScope
    fun bindsCharacterListUseCase(
        charactersListUseCaseImpl: CharactersListUseCaseImpl
    ): CharactersListUseCase

    @Binds
    fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory
}