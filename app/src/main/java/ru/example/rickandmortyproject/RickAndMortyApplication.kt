package ru.example.rickandmortyproject

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import ru.example.rickandmortyproject.di.App
import ru.example.rickandmortyproject.di.AppComponent

class RickAndMortyApplication : Application(), App {
    private var appComponent: AppComponent? = null

    private val cicerone = Cicerone.create()

    val router get() = cicerone.router

    val navigatorHolder get() = cicerone.getNavigatorHolder()

    override fun appComponent() = getAppComponent()

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent()
    }

    private fun getAppComponent(): AppComponent {
        if (appComponent == null) {
            appComponent = AppComponent.init(applicationContext)
        }
        return appComponent!!
    }

    companion object {
        lateinit var instance: RickAndMortyApplication
            private set
    }
}
