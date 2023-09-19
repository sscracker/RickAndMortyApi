package ru.example.rickandmortyproject.di.module

import dagger.Module
import dagger.Provides
import ru.example.rickandmortyproject.data.characters.list.mapper.CharacterDataToListSingleCharacterDomainMapper
import ru.example.rickandmortyproject.presentation.characters.list.mapper.SingleCharacterDomainToSingleCharacterUiMapper

@Module
class MapperModule {
    @Provides
    fun provideCharactersDataToListSingleCharacterDomainMapper(): CharacterDataToListSingleCharacterDomainMapper {
        return CharacterDataToListSingleCharacterDomainMapper()
    }

    @Provides
    fun provideSingleCharacterDomainToSingleCharacterUiMapper(): SingleCharacterDomainToSingleCharacterUiMapper {
        return SingleCharacterDomainToSingleCharacterUiMapper()
    }
}