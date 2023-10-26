package ru.example.rickandmortyproject.data.db.lists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.example.rickandmortyproject.data.characters.list.model.SingleCharacterEntity

@Dao
interface CharacterListDao {
    @Query("SELECT * FROM CharacterListEntity")
    fun getCharacterList(): List<SingleCharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCharacterList(characterList: List<SingleCharacterEntity>)

    @Update
    fun updateCharacterList(characterList: List<SingleCharacterEntity>)

    @Query("DELETE FROM CharacterListEntity")
    fun deleteCharacterList()
}