package ru.example.rickandmortyproject.utils

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> Fragment.viewModelFactory(
    noinline factory: () -> VM
): Lazy<VM> = viewModels {
    ViewModelFactoryAssisted(factory)
}

inline fun <reified VM : ViewModel> AppCompatActivity.viewModelFactory(
    noinline factory: () -> VM
): Lazy<VM> = viewModels {
    ViewModelFactoryAssisted(factory)
}

class ViewModelFactoryAssisted<VM : ViewModel>(
    private val factory: () -> VM
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        factory() as T
}
