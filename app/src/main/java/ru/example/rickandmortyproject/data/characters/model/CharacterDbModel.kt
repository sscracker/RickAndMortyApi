package ru.example.rickandmortyproject.data.characters.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CharacterListEntity")
data class CharacterDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: Int,
    val species: String,
    val gender: Int,
    val type: String,
    val image: String,
    val url: String,
    val created: String
)
