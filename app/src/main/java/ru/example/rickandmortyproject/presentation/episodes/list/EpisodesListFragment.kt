package ru.example.rickandmortyproject.presentation.episodes.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentCharactersBinding
import ru.example.rickandmortyproject.databinding.FragmentEpisodesBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.episodes.list.EpisodeEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.episodes.list.adapter.EpisodesListAdapter
import ru.example.rickandmortyproject.utils.showToast

private const val COLUMN_COUNT = 2

class EpisodesListFragment : BaseFragment<EpisodesListViewModel>(EpisodesListViewModel::class.java) {
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
        subscribeEpisodesFlow()
        notifyViewModel()
        startProgress()
    }

    private fun notifyViewModel(){
        viewModel.onViewCreated()
    }

    private fun initEpisodesList() {
        with(binding.recyclerViewEpisodes) {
            layoutManager = GridLayoutManager(requireActivity(), COLUMN_COUNT)
            adapter = episodesAdapter
        }
    }

    private fun subscribeEpisodesFlow(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.episodesListState.onEach {
                    processEpisodesList(it)
                }.launchIn(this)

                viewModel.errorState.onEach {
                    showError()
                    stopProgress()
                }.launchIn(this)
            }
        }
    }

    private fun processEpisodesList(episodes: List<EpisodeEntity>){
        episodesAdapter.submitList(episodes)
        stopProgress()
    }

    private fun startProgress(){
        binding.progressBar.isVisible = true
    }

    private fun stopProgress(){
        binding.progressBar.isVisible = false
    }

    private fun configSwipeRefreshLayout(){
        binding.swipeRefreshLayoutEpisodesList.setProgressViewEndTarget(false, 0)
    }

    private fun setOnRefreshListener(){
        binding.swipeRefreshLayoutEpisodesList.setOnRefreshListener {
            viewModel.onListSwiped()
            startProgress()
        }
    }

    private fun showError(){
        val message = "Pull the list and retry loading"
        requireContext().showToast(message)
    }

    companion object {
        private const val KEY_TAB_NAME = "tabName"

        fun newInstance(tabName: String) = EpisodesListFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}