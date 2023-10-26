package ru.example.rickandmortyproject.data.characters.list.api

import retrofit2.Response
import retrofit2.http.GET
import ru.example.rickandmortyproject.data.characters.list.model.CharactersData

interface CharactersApi {
    @GET("character")
    suspend fun getAllCharacters(): Response<CharactersData>
}