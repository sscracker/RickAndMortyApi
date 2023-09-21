package ru.example.rickandmortyproject.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.example.rickandmortyproject.di.module.LocalDbModule
import ru.example.rickandmortyproject.di.module.MapperModule
import ru.example.rickandmortyproject.di.module.NetworkModule
import ru.example.rickandmortyproject.di.module.BindsModule
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.presentation.characters.list.CharactersListFragment
import ru.example.rickandmortyproject.utils.ViewModelFactory

@Component(
    modules = [
        NetworkModule::class,
        MapperModule::class,
        LocalDbModule::class,
        BindsModule::class,
        UseCaseModule::class
    ]
)
@ActivityScope
interface AppComponent {
    fun inject(charactersListFragment: CharactersListFragment)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun getViewModelFactory(): ViewModelFactory
}