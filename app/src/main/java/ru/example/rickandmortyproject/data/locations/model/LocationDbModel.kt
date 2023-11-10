package ru.example.rickandmortyproject.data.locations.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationsListEntity")
data class LocationDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val characterId: String,
    val url: String,
    val created: String
)
