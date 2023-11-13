package ru.example.rickandmortyproject.domain.characters.list.usecases

interface LoadCharactersByIdUseCase {
    suspend operator fun invoke(ids: List<Int>): Boolean
}