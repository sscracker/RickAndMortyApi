package ru.example.rickandmortyproject.data.db.lists

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.example.rickandmortyproject.data.characters.model.CharacterDbModel
import ru.example.rickandmortyproject.data.db.BaseDao

@Dao
interface CharacterListDao: BaseDao<CharacterDbModel> {
    @Query("SELECT * FROM CharacterListEntity ORDER BY id")
    fun getCharacterList(): Flow<List<CharacterDbModel>>

    @Query("SELECT * FROM CharacterListEntity WHERE id == :itemId LIMIT 1")
    fun getItem(itemId: Int): Flow<CharacterDbModel>

    @Query("SELECT * FROM CharacterListEntity WHERE id IN (:ids)")
    fun getItemsByIds(ids: List<Int>): Flow<List<CharacterDbModel>>
}