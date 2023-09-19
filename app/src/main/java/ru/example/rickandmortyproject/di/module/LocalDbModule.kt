package ru.example.rickandmortyproject.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.example.rickandmortyproject.data.db.AppDatabase
import ru.example.rickandmortyproject.data.db.lists.CharacterListDao
import ru.example.rickandmortyproject.di.scope.ActivityScope

@Module
class LocalDbModule {
    @Provides
    @ActivityScope
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()

    @Provides
    @ActivityScope
    fun provideCharacterListDao(appDatabase: AppDatabase): CharacterListDao =
        appDatabase.characterListDao()
}