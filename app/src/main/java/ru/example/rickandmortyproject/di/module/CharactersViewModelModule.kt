package ru.example.rickandmortyproject.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.example.rickandmortyproject.di.key.ViewModelKey
import ru.example.rickandmortyproject.domain.characters.list.CharactersListUseCase
import ru.example.rickandmortyproject.presentation.characters.list.CharacterListViewModel
import ru.example.rickandmortyproject.presentation.characters.list.mapper.SingleCharacterDomainToSingleCharacterUiMapper
import ru.example.rickandmortyproject.utils.Connectivity

@Module
class CharactersViewModelModule {

    @IntoMap
    @ViewModelKey(CharacterListViewModel::class)
    @Provides
    fun provideCharacterListViewModel(
        charactersListUseCase: CharactersListUseCase,
        mapper: SingleCharacterDomainToSingleCharacterUiMapper,
        connectivity: Connectivity
    ): ViewModel {
        return CharacterListViewModel(
            charactersListUseCase,
            mapper,
            connectivity
        )
    }
}