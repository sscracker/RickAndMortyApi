package ru.example.rickandmortyproject.di.module

import dagger.Binds
import dagger.Module
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.domain.characters.list.CharactersListUseCase
import ru.example.rickandmortyproject.domain.characters.list.CharactersListUseCaseImpl

@Module
interface UseCaseModule {
    @Binds
    @ActivityScope
    fun bindsCharacterListUseCase(
        charactersListUseCaseImpl: CharactersListUseCaseImpl
    ): CharactersListUseCase
}