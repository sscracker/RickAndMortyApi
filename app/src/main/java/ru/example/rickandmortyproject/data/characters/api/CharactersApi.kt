package ru.example.rickandmortyproject.data.characters.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.example.rickandmortyproject.data.characters.model.CharacterDto

interface CharactersApi {
    @GET("character")
    suspend fun loadPage(
        @Query(QUERY_PARAM_PAGE) page: Int
    ): ResponseDto

    @GET("character/{$PATH_ITEM_ID}")
    suspend fun loadItem(
        @Path(PATH_ITEM_ID) itemId: Int
    ): CharacterDto

    @GET("character/{${PATH_ITEM_IDS_STRING}}")
    suspend fun loadItemsByIds(
        @Path(PATH_ITEM_IDS_STRING) itemIds: String
    ): List<CharacterDto>

    companion object {
        private const val QUERY_PARAM_PAGE = "page"
        private const val PATH_ITEM_ID = "itemId"
        private const val PATH_ITEM_IDS_STRING = "itemIdsString"
    }
}