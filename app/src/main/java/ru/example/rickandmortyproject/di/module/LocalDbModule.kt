package ru.example.rickandmortyproject.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.example.rickandmortyproject.data.db.AppDatabase
import ru.example.rickandmortyproject.di.scope.ActivityScope

private const val DB_NAME = "app_db"

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
    fun provideCharacterListDao(appDatabase: AppDatabase) = appDatabase.charactersListDao()

    @Provides
    @ActivityScope
    fun provideEpisodeListDao(appDatabase: AppDatabase) = appDatabase.episodesListDao()

    @Provides
    @ActivityScope
    fun provideLocationListDao(appDatabase: AppDatabase) = appDatabase.locationsListDao()
}
