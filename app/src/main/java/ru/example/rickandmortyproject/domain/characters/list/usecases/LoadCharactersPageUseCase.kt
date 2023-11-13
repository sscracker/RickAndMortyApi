package ru.example.rickandmortyproject.domain.characters.list.usecases

interface LoadCharactersPageUseCase {
    suspend operator fun invoke(pageNumber: Int): Boolean
}