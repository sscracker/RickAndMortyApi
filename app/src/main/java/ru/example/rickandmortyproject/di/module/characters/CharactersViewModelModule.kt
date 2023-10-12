package ru.example.rickandmortyproject.di.module.characters

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.example.rickandmortyproject.di.key.ViewModelKey
import ru.example.rickandmortyproject.presentation.characters.details.CharacterDetailsViewModel
import ru.example.rickandmortyproject.presentation.characters.list.CharacterListViewModel
import ru.example.rickandmortyproject.presentation.characters.list.CharactersFiltersViewModel

@Module
interface CharactersViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CharacterListViewModel::class)
    fun bindCharactersViewModel(charactersViewModel: CharacterListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharactersFiltersViewModel::class)
    fun bindCharactersFilterViewModel(charactersFilterViewModel: CharactersFiltersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharacterDetailsViewModel::class)
    fun bindCharacterDetailsViewModel(characterDetailsViewModel: CharacterDetailsViewModel): ViewModel

}