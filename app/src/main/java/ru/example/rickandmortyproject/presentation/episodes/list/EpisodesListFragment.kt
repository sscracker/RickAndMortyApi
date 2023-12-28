package ru.example.rickandmortyproject.presentation.episodes.list

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
import androidx.recyclerview.widget.GridLayoutManager
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.RickAndMortyApplication
import ru.example.rickandmortyproject.databinding.FragmentEpisodesBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.episodes.list.adapter.EpisodesListAdapter
import ru.example.rickandmortyproject.utils.Screens
import ru.example.rickandmortyproject.utils.showToast
import ru.example.rickandmortyproject.utils.viewModelFactory

private const val COLUMN_COUNT = 2

class EpisodesListFragment : BaseFragment() {
    private var _binding: FragmentEpisodesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelProvider: Provider<EpisodesListViewModel>
    private val viewModel: EpisodesListViewModel by viewModelFactory {
        viewModelProvider.get()
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
        binding.episodesFilterButton.setOnClickListener {
            RickAndMortyApplication.instance.router.navigateTo(Screens.fragmentEpisodeFilters())
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
        RickAndMortyApplication.instance.router.navigateTo(Screens.fragmentEpisodeDetails(id))
    }

    companion object {
        const val KEY_FILTER_CHANGED = "episodesFilterChanged"

        fun newInstance() = EpisodesListFragment()
    }
}
