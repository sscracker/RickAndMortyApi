package ru.example.rickandmortyproject.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.example.rickandmortyproject.di.module.LocalDbModule
import ru.example.rickandmortyproject.di.module.NetworkModule
import ru.example.rickandmortyproject.di.module.characters.CharactersBindsModule
import ru.example.rickandmortyproject.di.module.characters.CharactersViewModelModule
import ru.example.rickandmortyproject.di.module.episodes.EpisodesBindsModule
import ru.example.rickandmortyproject.di.module.factory.ViewModelFactoryModule
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.presentation.characters.details.CharacterDetailsFragment
import ru.example.rickandmortyproject.presentation.characters.list.CharactersFiltersFragment
import ru.example.rickandmortyproject.presentation.characters.list.CharactersListFragment

@Component(
    modules = [
        NetworkModule::class,
        LocalDbModule::class,
        CharactersBindsModule::class,
        CharactersViewModelModule::class,
        EpisodesBindsModule::class,
        ViewModelFactoryModule::class
    ]
)
@ActivityScope
interface AppComponent {
    fun inject(charactersListFragment: CharactersListFragment)
    fun inject(charactersFiltersFragment: CharactersFiltersFragment)
    fun inject(characterDetailsFragment: CharacterDetailsFragment)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    companion object {
        fun init(context: Context): AppComponent {
            return DaggerAppComponent.factory().create(context)
        }
    }
}