package ru.example.rickandmortyproject.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.example.rickandmortyproject.di.module.LocalDbModule
import ru.example.rickandmortyproject.di.module.NetworkModule
import ru.example.rickandmortyproject.di.module.characters.CharactersBindsModule
import ru.example.rickandmortyproject.di.module.episodes.EpisodesBindsModule
import ru.example.rickandmortyproject.di.module.locations.LocationsBindsModule
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.presentation.characters.details.CharacterDetailsFragment
import ru.example.rickandmortyproject.presentation.characters.list.CharactersFiltersFragment
import ru.example.rickandmortyproject.presentation.characters.list.CharactersListFragment
import ru.example.rickandmortyproject.presentation.episodes.details.EpisodeDetailsFragment
import ru.example.rickandmortyproject.presentation.episodes.list.EpisodesFilterFragment
import ru.example.rickandmortyproject.presentation.episodes.list.EpisodesListFragment
import ru.example.rickandmortyproject.presentation.locations.details.LocationDetailsFragment
import ru.example.rickandmortyproject.presentation.locations.list.LocationsFilterFragment
import ru.example.rickandmortyproject.presentation.locations.list.LocationsListFragment

@Component(
    modules = [
        NetworkModule::class,
        LocalDbModule::class,
        CharactersBindsModule::class,
        EpisodesBindsModule::class,
        LocationsBindsModule::class,
    ],
)
@ActivityScope
interface AppComponent {
    fun inject(charactersListFragment: CharactersListFragment)

    fun inject(charactersFiltersFragment: CharactersFiltersFragment)

    fun inject(characterDetailsFragment: CharacterDetailsFragment)

    fun inject(episodesListFragment: EpisodesListFragment)

    fun inject(episodesFilterFragment: EpisodesFilterFragment)

    fun inject(episodeDetailsFragment: EpisodeDetailsFragment)

    fun inject(locationsListFragment: LocationsListFragment)

    fun inject(locationsFilterFragment: LocationsFilterFragment)

    fun inject(locationDetailsFragment: LocationDetailsFragment)

    @Component.Factory
    interface AppComponentFactory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }

    companion object {
        fun init(context: Context): AppComponent {
            return DaggerAppComponent.factory().create(context)
        }
    }
}
