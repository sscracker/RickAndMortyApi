package ru.example.rickandmortyproject.presentation.locations.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
        startProgress()
        setAdapter()
        notifyViewModel()
        configSwipeRefreshLayout()
        setOnRefreshListener()
        subscribeLocationsFlow()
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
                }.launchIn(this)

                viewModel.emptyStateFlow.onEach {
                    showEmptyResultToast()
                }.launchIn(this)
            }
        }
    }

    private fun processLocationsList(locations: List<LocationEntity>) {
        locationsAdapter.submitList(locations)
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

        fun newInstance(tabName: String) = LocationsListFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}
