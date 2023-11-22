package ru.example.rickandmortyproject.presentation.locations.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.databinding.FragmentLocationFiltersBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.utils.viewModelFactory

class LocationsFilterFragment : BaseFragment() {

    private var _binding: FragmentLocationFiltersBinding? = null
    private val binding get() = _binding!!

    private var restored: Boolean = false

    @Inject
    lateinit var viewModelProvider: Provider<LocationsFilterViewModel>
    private val viewModel: LocationsFilterViewModel by viewModelFactory {
        viewModelProvider.get()
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeData()
        initListeners()
    }

    private fun subscribeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLocationsFilterStateFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { settings ->
                    if (!restored) {
                        setLocationFilterSettings(settings)
                    }
                }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.saveLocationsFilterStateFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        saveLocationsFilterSettings()
                    }
            }
        }
    }

    private fun initListeners() {
        binding.locationFilterBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.locationsFilterApplyButton.setOnClickListener {
            val settings = currentLocationSettings()
            viewModel.onApplyClick(settings)
        }
    }

    private fun currentLocationSettings(): LocationFilterSettings {
        val name = binding.locationFilterNameEditText.text.toString()
        val type = binding.locationsFilterTypeEditText.text.toString()
        val dimension = binding.locationsFilterDimensionEditText.text.toString()
        return LocationFilterSettings(
            name,
            type,
            dimension
        )
    }

    private fun setLocationFilterSettings(settings: LocationFilterSettings) {
        binding.locationFilterNameEditText.setText(settings.name)
        binding.locationsFilterTypeEditText.setText(settings.type)
        binding.locationsFilterDimensionEditText.setText(settings.dimension)
    }

    private fun saveLocationsFilterSettings() {
        setFragmentResult(
            LocationsListFragment.KEY_FILTER_CHANGED,
            bundleOf(LocationsListFragment.KEY_FILTER_CHANGED to true)
        )
        requireActivity().supportFragmentManager.popBackStack()
    }

    companion object {
        fun newInstance() = LocationsFilterFragment()
    }
}
