package ru.example.rickandmortyproject.presentation.locations.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.RickAndMortyApplication
import ru.example.rickandmortyproject.databinding.FragmentLocationsBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.locations.list.adapter.LocationsListAdapter
import ru.example.rickandmortyproject.utils.Screens
import ru.example.rickandmortyproject.utils.showToast
import ru.example.rickandmortyproject.utils.viewModelFactory

class LocationsListFragment : BaseFragment() {
    private var _binding: FragmentLocationsBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelProvider: Provider<LocationsListViewModel>
    private val viewModel: LocationsListViewModel by viewModelFactory {
        viewModelProvider.get()
    }

    private val locationsAdapter by lazy {
        LocationsListAdapter(
            onItemClick = { location -> launchLocationDetailsFragment(location.id) },
            onListEnded = { viewModel.onListEnded() }
        )
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
        initListeners()
        notifyViewModel()
        startProgress()
    }

    private fun launchLocationDetailsFragment(id: Int) {
        RickAndMortyApplication.instance.router.navigateTo(Screens.fragmentLocationDetails(id))
    }

    private fun initListeners() {
        binding.locationsFilterButton.setOnClickListener {
            RickAndMortyApplication.instance.router.navigateTo(Screens.fragmentLocationFilters())
        }

        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.onSearchQueryChanged(query)
                return true
            }
        }
        binding.locationsSearchView.setOnQueryTextListener(listener)

        setFragmentResultListener(KEY_FILTER_CHANGED) { key, bundle ->
            val isChanged = bundle.getBoolean(key)
            if (isChanged) {
                viewModel.onFilterSettingsChanged()
            }
        }
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

        fun newInstance() = LocationsListFragment()
    }
}
