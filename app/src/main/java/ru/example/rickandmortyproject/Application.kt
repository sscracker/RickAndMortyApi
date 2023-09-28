package ru.example.rickandmortyproject

import android.app.Application
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.di.App

class Application : Application(), App {
    private var appComponent: AppComponent? = null

    override fun appComponent() = getAppComponent()

    override fun onCreate() {
        super.onCreate()
        appComponent()
    }

    private fun getAppComponent(): AppComponent{
        if (appComponent == null){
            appComponent = AppComponent.init(applicationContext)
        }
        return appComponent!!
    }
}