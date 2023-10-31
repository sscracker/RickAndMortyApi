package ru.example.rickandmortyproject.data.characters.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationMinDto(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("url")
    @Expose
    val url: String
)
