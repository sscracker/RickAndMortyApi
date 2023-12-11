package ru.example.rickandmortyproject.data.episodes.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.example.rickandmortyproject.data.characters.api.ResponseDto
import ru.example.rickandmortyproject.data.episodes.model.EpisodesDto

interface EpisodesApi {
    @GET("episode")
    suspend fun loadPage(
        @Query("page") page: Int,
    ): ResponseDto

    @GET("episode/itemId")
    suspend fun loadItem(
        @Path("itemId") itemId: Int,
    ): EpisodesDto

    @GET("episode/itemIdsString")
    suspend fun loadEpisodesByIds(
        @Path("itemIdsString") itemIds: String,
    ): List<EpisodesDto>
}
