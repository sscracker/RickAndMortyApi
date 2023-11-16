package ru.example.rickandmortyproject.presentation.locations.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentLocationsBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.locations.list.adapter.LocationsListAdapter
import ru.example.rickandmortyproject.utils.showToast

class LocationsListFragment :
    BaseFragment<LocationsListViewModel>(LocationsListViewModel::class.java) {
    private var _binding: FragmentLocationsBinding? = null

    private val binding get() = _binding!!

    private val locationsAdapter by lazy {
        LocationsListAdapter(
            onListEnded = { viewModel.onListEnded() }
        )
    }

    private val tabName by lazy {
        requireArguments().getString(KEY_TAB_NAME)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        configSwipeRefreshLayout()
        setOnRefreshListener()
        subscribeLocationsFlow()
        setFilterButtonClickListener()
        setFilterResultListener()
        setSearchViewListener()
        notifyViewModel()
        startProgress()
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    private fun subscribeLocationsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationsListSharedFlow.onEach { locations ->
                    processLocationsList(locations)
                }.launchIn(this)

                viewModel.errorStateFlow.onEach {
                    showErrorToast()
                    stopProgress()
                }.launchIn(this)

                viewModel.notEmptyFilterStateFlow.onEach {
                    setClearButtonClickListener(it)
                }.launchIn(this)
            }
        }
    }

    private fun setClearButtonClickListener(notEmptyLocationFilter: Boolean) {
        if (notEmptyLocationFilter) {
            binding.locationsFilterClearButton.setBackgroundResource(
                R.drawable.app_rectangle_button
            )
            binding.locationsFilterClearButton.setOnClickListener {
                viewModel.onFilterClearButtonClick()
                startProgress()
            }
        } else {
            binding.locationsFilterClearButton.setBackgroundResource(R.drawable.app_gray_button)
            binding.locationsFilterClearButton.setOnClickListener(null)
        }
    }

    private fun setSearchViewListener() {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.onSearchQueryChanged(query)
                return true
            }
        }
        binding.locationsSearchView.setOnQueryTextListener(listener)
    }

    private fun setFilterButtonClickListener() {
        binding.locationsFilterButton.setOnClickListener {
            tabName?.let {
                parentFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, LocationsFilterFragment.newInstance())
                    .addToBackStack(it)
                    .commit()
            }
        }
    }

    private fun setFilterResultListener() {
        setFragmentResultListener(KEY_FILTER_CHANGED) { key, bundle ->
            val isChanged = bundle.getBoolean(key)
            if (isChanged) {
                viewModel.onFilterSettingsChanged()
            }
        }
    }

    private fun processLocationsList(locations: List<LocationEntity>) {
        locationsAdapter.submitList(locations)
        if (locations.isEmpty()) {
            showEmptyResultToast()
        }
        stopProgress()
    }

    private fun showErrorToast() {
        requireContext().showToast(requireContext().getString(R.string.pull_the_list))
    }

    private fun showEmptyResultToast() {
        requireContext().showToast(requireContext().getString(R.string.empty_list))
    }

    private fun configSwipeRefreshLayout() {
        binding.locationsListSwipeRefreshLayout.setProgressViewEndTarget(false, 0)
    }

    private fun setOnRefreshListener() {
        binding.locationsListSwipeRefreshLayout.setOnRefreshListener {
            viewModel.onListSwiped()
            startProgress()
        }
    }

    private fun setAdapter() {
        binding.locationsListRecyclerView.adapter = locationsAdapter
    }

    private fun startProgress() {
        binding.locationsListProgressBar.isVisible = true
    }

    private fun stopProgress() {
        binding.locationsListProgressBar.isVisible = false
    }

    companion object {
        private const val KEY_TAB_NAME = "tabName"
        const val KEY_FILTER_CHANGED = "locationFiltersChanged"

        fun newInstance(tabName: String) = LocationsListFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}
