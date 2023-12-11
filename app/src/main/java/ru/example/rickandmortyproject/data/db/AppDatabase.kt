package ru.example.rickandmortyproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.example.rickandmortyproject.data.characters.model.CharacterDbModel
import ru.example.rickandmortyproject.data.db.lists.CharacterListDao
import ru.example.rickandmortyproject.data.db.lists.EpisodeListDao
import ru.example.rickandmortyproject.data.db.lists.LocationsListDao
import ru.example.rickandmortyproject.data.episodes.model.EpisodeDbModel
import ru.example.rickandmortyproject.data.locations.model.LocationDbModel

@Database(
    entities = [
        CharacterDbModel::class,
        EpisodeDbModel::class,
        LocationDbModel::class,
    ],
    version = 4,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersListDao(): CharacterListDao

    abstract fun episodesListDao(): EpisodeListDao

    abstract fun locationsListDao(): LocationsListDao
}
