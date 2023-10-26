package ru.example.rickandmortyproject.data.characters.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("info")
    @Expose
    val info: JsonObject,
    @SerializedName("results")
    @Expose
    val results: JsonArray
)