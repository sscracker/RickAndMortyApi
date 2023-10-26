package ru.example.rickandmortyproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.example.rickandmortyproject.data.characters.model.CharacterDbModel
import ru.example.rickandmortyproject.data.db.lists.CharacterListDao
import ru.example.rickandmortyproject.data.db.lists.EpisodeListDao
import ru.example.rickandmortyproject.data.episodes.model.EpisodeDbModel

@Database(
    entities = [
        CharacterDbModel::class,
        EpisodeDbModel::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersListDao(): CharacterListDao
    abstract fun episodesListDao(): EpisodeListDao
}