package ru.example.rickandmortyproject.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.example.rickandmortyproject.data.db.AppDatabase
import ru.example.rickandmortyproject.data.db.lists.CharacterListDao
import ru.example.rickandmortyproject.di.scope.ActivityScope
import javax.inject.Singleton

private const val DB_NAME = "app_dp"
@Module
class LocalDbModule {
    @Provides
    @ActivityScope
    fun provideAppDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).build()

    @Provides
    @ActivityScope
    fun provideCharacterListDao(appDatabase: AppDatabase) = appDatabase.characterListDao()
}