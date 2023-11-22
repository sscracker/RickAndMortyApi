package ru.example.rickandmortyproject.presentation.base

import android.content.Context
import androidx.fragment.app.Fragment
import ru.example.rickandmortyproject.di.App
import ru.example.rickandmortyproject.di.AppComponent

abstract class BaseFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies((requireActivity().application as App).appComponent())
    }

    abstract fun injectDependencies(appComponent: AppComponent)
}
