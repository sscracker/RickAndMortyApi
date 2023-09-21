package ru.example.rickandmortyproject.data.characters.list.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CharactersData(
    @Expose
    val info: CharactersPaginationData,

    @SerializedName("results")
    @Expose
    val characters: List<SingleCharacterEntity>
)