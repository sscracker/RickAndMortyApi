package ru.example.rickandmortyproject.data.episodes.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.example.rickandmortyproject.data.characters.api.ResponseDto
import ru.example.rickandmortyproject.data.episodes.model.EpisodesDto

interface EpisodesApi {

    @GET("episode")
    suspend fun loadPage(
        @Query(QUERY_PARAM_PAGE) page: Int
    ): ResponseDto

    @GET("episode/{$PATH_ITEM_ID}")
    suspend fun loadItem(
        @Path(PATH_ITEM_ID) itemId: Int
    ): EpisodesDto

    @GET("episode/{$PATH_ITEM_IDS_STRING}")
    suspend fun loadEpisodesByIds(
        @Path(PATH_ITEM_IDS_STRING) itemIds: String
    ): List<EpisodesDto>

    companion object {

        private const val QUERY_PARAM_PAGE = "page"
        private const val PATH_ITEM_ID = "itemId"
        private const val PATH_ITEM_IDS_STRING = "itemIdsString"
    }
}