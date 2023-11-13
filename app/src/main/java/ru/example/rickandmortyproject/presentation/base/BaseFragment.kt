package ru.example.rickandmortyproject.presentation.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import ru.example.rickandmortyproject.di.App
import ru.example.rickandmortyproject.di.AppComponent

abstract class BaseFragment<VM : ViewModel>(private val viewModelClass: Class<VM>) : Fragment() {

    private var _viewModel: VM? = null
    protected val viewModel get() = requireNotNull(_viewModel) { "ViewModel isn't init" }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies((requireActivity().application as App).appComponent())

        _viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[viewModelClass]
    }

    abstract fun injectDependencies(appComponent: AppComponent)
}
