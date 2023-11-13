package ru.example.rickandmortyproject.presentation.characters

import ru.example.rickandmortyproject.presentation.characters.list.model.SingleCharacter

interface CharacterListDetailsListener {
    fun onCharacterClick(character: SingleCharacter)
}