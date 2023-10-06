package ru.example.rickandmortyproject.presentation.characters.list

import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import javax.inject.Inject

class CharactersMatcher @Inject constructor() {

    fun isCharacterMatches(
        filter: CharacterFilterSettings,
        character: CharacterEntity
    ): Boolean {
        val nameMatches = if (filter.name != EMPTY_STRING) {
            character.name.contains(filter.name, true)
        } else {
            true
        }

        val statusMatches = filter.status?.let {
            character.status == it
        } ?: true

        val speciesMatches = if (filter.species != EMPTY_STRING) {
            character.species.contains(filter.species, true)
        } else {
            true
        }

        val typeMatches = if (filter.type != EMPTY_STRING) {
            character.type.contains(filter.type, true)
        } else {
            true
        }

        val genderMatches = filter.gender?.let {
            character.gender == it
        } ?: true

        return nameMatches && statusMatches && speciesMatches && typeMatches && genderMatches
    }

    companion object {

        private const val EMPTY_STRING = ""
    }
}