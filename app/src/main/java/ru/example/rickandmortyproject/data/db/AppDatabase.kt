package ru.example.rickandmortyproject.data.db

import androidx.room.Database
import ru.example.rickandmortyproject.data.characters.list.model.SingleCharacterEntity
import ru.example.rickandmortyproject.data.db.lists.CharacterListDao

@Database(
    entities = [
        SingleCharacterEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase {
    abstract fun characterListDao(): CharacterListDao
}