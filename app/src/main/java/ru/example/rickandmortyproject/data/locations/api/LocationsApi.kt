package ru.example.rickandmortyproject.data.locations.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.example.rickandmortyproject.data.characters.api.ResponseDto
import ru.example.rickandmortyproject.data.locations.model.LocationsDto

interface LocationsApi {
    @GET("location")
    suspend fun loadPage(
        @Query("page") page: Int,
    ): ResponseDto

    @GET("episode/itemId")
    suspend fun loadSingleLocation(
        @Path("itemId") itemId: Int,
    ): LocationsDto

    @GET("episode/itemIdsString")
    suspend fun loadLocationsByIds(
        @Path("itemIdsString") itemIds: String,
    ): List<LocationsDto>
}
