package ru.example.rickandmortyproject.presentation.episodes.list

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
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentEpisodesBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.episodes.details.EpisodeDetailsFragment
import ru.example.rickandmortyproject.presentation.episodes.list.adapter.EpisodesListAdapter
import ru.example.rickandmortyproject.utils.showToast

private const val COLUMN_COUNT = 2

class EpisodesListFragment :
    BaseFragment<EpisodesListViewModel>(EpisodesListViewModel::class.java) {
    private var _binding: FragmentEpisodesBinding? = null
    private val binding get() = _binding!!

    private val tabName by lazy {
        requireArguments().getString(KEY_TAB_NAME)
    }
    private val episodesAdapter by lazy {
        EpisodesListAdapter(
            onListEnded = {
                viewModel.onListEnded()
                startProgress()
            },
            onItemClick = { episode ->
                launchEpisodeDetailsFragment(episode.id)
            }
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
        _binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEpisodesList()
        configSwipeRefreshLayout()
        setOnRefreshListener()
        setButtonFilterListener()
        setFilterResultListener()
        setSearchViewListener()
        subscribeEpisodesFlow()
        startProgress()
        notifyViewModel()
        startProgress()
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }
    private fun initEpisodesList() {
        with(binding.recyclerViewEpisodes) {
            layoutManager = GridLayoutManager(requireActivity(), COLUMN_COUNT)
            adapter = episodesAdapter
        }
    }

    private fun subscribeEpisodesFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.episodesListStateFlow.onEach {
                    processEpisodesList(it)
                }.launchIn(this)

                viewModel.notEmptyFilterStateFlow.onEach {
                    setButtonClearState(it)
                }.launchIn(this)

                viewModel.errorStateFlow.onEach {
                    showErrorToast()
                    stopProgress()
                }.launchIn(this)

                viewModel.emptyStateFlow.onEach {
                    showEmptyResultToast()
                }.launchIn(this)
            }
        }
    }

    private fun setButtonClearState(notEmptyFilter: Boolean) {
        if (notEmptyFilter) {
            binding.episodesFilterClearButton.setBackgroundResource(R.drawable.app_rectangle_button)
            binding.episodesFilterClearButton.setOnClickListener {
                viewModel.onButtonClearPressed()
                startProgress()
            }
        } else {
            binding.episodesFilterClearButton.setBackgroundResource(R.drawable.app_gray_button)
            binding.episodesFilterClearButton.setOnClickListener(null)
        }
    }

    private fun processEpisodesList(episodes: List<EpisodeEntity>) {
        episodesAdapter.submitList(episodes)
        stopProgress()
    }

    private fun startProgress() {
        binding.progressBar.isVisible = true
    }

    private fun stopProgress() {
        binding.progressBar.isVisible = false
    }

    private fun configSwipeRefreshLayout() {
        binding.swipeRefreshLayoutEpisodesList.setProgressViewEndTarget(false, 0)
    }

    private fun setOnRefreshListener() {
        binding.swipeRefreshLayoutEpisodesList.setOnRefreshListener {
            viewModel.onListSwiped()
            startProgress()
        }
    }

    private fun showErrorToast() {
        val message = requireContext().getString(R.string.pull_the_list)
        requireContext().showToast(message)
    }

    private fun showEmptyResultToast() {
        val message = requireContext().getString(R.string.empty_list)
        requireContext().showToast(message)
    }

    private fun setFilterResultListener() {
        setFragmentResultListener(KEY_FILTER_CHANGED) { key, bundle ->
            val isChanged = bundle.getBoolean(key)
            if (isChanged) {
                viewModel.onFilterSettingsChanged()
            }
        }
    }

    private fun setButtonFilterListener() {
        binding.episodesFilterButton.setOnClickListener {
            launchFilterFragment()
        }
    }

    private fun launchFilterFragment() {
        tabName?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, EpisodesFilterFragment.newInstance())
                .addToBackStack(it)
                .commit()
        }
    }

    private fun setSearchViewListener() {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(changedQuery: String?): Boolean {
                viewModel.onSearchQueryChanged(changedQuery)
                return true
            }
        }
        binding.episodesSearchView.setOnQueryTextListener(listener)
    }

    private fun launchEpisodeDetailsFragment(id: Int) {
        tabName?.let { episodeDetailsTabName ->
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.fragment_container,
                    EpisodeDetailsFragment.newInstance(id, episodeDetailsTabName)
                )
                .addToBackStack(episodeDetailsTabName)
                .commit()
        }
    }

    companion object {
        const val KEY_FILTER_CHANGED = "episodesFilterChanged"
        private const val KEY_TAB_NAME = "tabName"

        fun newInstance(tabName: String) = EpisodesListFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}
