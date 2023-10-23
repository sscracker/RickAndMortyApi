package ru.example.rickandmortyproject.data.db.lists

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.data.db.BaseDao
import ru.example.rickandmortyproject.data.episodes.model.EpisodeDbModel

@Dao
interface EpisodeListDao : BaseDao<EpisodeDbModel> {

    @Query("SELECT * FROM EpisodesListEntity ORDER BY id")
    fun getAllEpisodes(): Flow<List<EpisodeDbModel>>

    @Query("SELECT * FROM EpisodesListEntity WHERE id == :itemId LIMIT 1")
    fun getSingleEpisode(itemId: Int): Flow<EpisodeDbModel>

    @Query("SELECT * FROM EpisodesListEntity WHERE id IN (:itemIds)")
    fun getEpisodesByIds(itemIds: List<Int>): Flow<List<EpisodeDbModel>>
}