package ru.example.rickandmortyproject.data.characters.list.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "CharacterListEntity")
data class SingleCharacterData(
    @Expose
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @Expose
    val name: String,
    @Expose
    val status: String,
    @Expose
    val species: String,
    @Expose
    val gender: String,
    @Expose
    val type: String,
    @Expose
    val image: String
)
