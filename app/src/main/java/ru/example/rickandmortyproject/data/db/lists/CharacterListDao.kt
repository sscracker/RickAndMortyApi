package ru.example.rickandmortyproject.data.db.lists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.example.rickandmortyproject.data.characters.list.model.SingleCharacterData

@Dao
interface CharacterListDao {
    @Query("SELECT * FROM CharacterListEntity")
    suspend fun getCharacterList(): List<SingleCharacterData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setCharacterList(characterList: List<SingleCharacterData>)

    @Update
    suspend fun updateCharacterList(characterList: List<SingleCharacterData>)

    @Query("DELETE FROM CharacterListEntity")
    suspend fun deleteCharacterList()
}