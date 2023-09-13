package ru.example.rickandmortyproject.presentation.characters

import ru.example.rickandmortyproject.presentation.characters.list.SingleCharacter

interface CharacterListDetailsListener {
    fun onCharacterClick(character: SingleCharacter)
}