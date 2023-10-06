package ru.example.rickandmortyproject.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.example.rickandmortyproject.data.db.AppDatabase
import ru.example.rickandmortyproject.data.db.lists.CharacterListDao
import javax.inject.Singleton

private const val DB_NAME = "app_dp"
@Module
class LocalDbModule {
    @Provides
    @Singleton
    fun provideAppDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideCharacterListDao(appDatabase: AppDatabase) = appDatabase.characterListDao()
}