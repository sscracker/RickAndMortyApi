package ru.example.rickandmortyproject.data.characters.list.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CharactersPaginationData(
    @Expose
    val count: Long,
    @Expose
    val pages: Long,
    @Expose
    val next: String,
    @SerializedName("prev")
    @Expose
    val previous: String
)