package ru.example.rickandmortyproject.data.db.lists

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.data.db.BaseDao
import ru.example.rickandmortyproject.data.locations.model.LocationDbModel

@Dao
interface LocationsListDao : BaseDao<LocationDbModel> {

    @Query("SELECT * FROM LocationsListEntity ORDER BY id")
    fun getAllLocations(): Flow<List<LocationDbModel>>

    @Query("SELECT * FROM LocationsListEntity WHERE id == :itemId LIMIT 1")
    fun getSingleLocation(itemId: Int): Flow<LocationDbModel>

    @Query("SELECT * FROM LocationsListEntity WHERE id in (:itemIds)")
    fun getLocationsByIds(itemIds: List<Int>): Flow<List<LocationDbModel>>
}
