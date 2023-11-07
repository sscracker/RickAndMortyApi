package ru.example.rickandmortyproject.presentation.episodes.details

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
import ru.example.rickandmortyproject.databinding.FragmentEpisodeDeatilsBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.characters.details.CharacterDetailsFragment
import ru.example.rickandmortyproject.presentation.characters.list.adapter.CharacterListAdapter

class EpisodeDetailsFragment :
    BaseFragment<EpisodesDetailsViewModel>(EpisodesDetailsViewModel::class.java) {

    private var _binding: FragmentEpisodeDeatilsBinding? = null

    private val binding get() = _binding!!

    private val characterListAdapter by lazy {
        CharacterListAdapter(
            onListEnded = null,
            onItemClick = { character ->
                launchCharacterDetailsFragment(character.id)
            }
        )
    }

    private var loadedCount = COUNT_START

    private var episode: EpisodeEntity? = null

    private val tabName by lazy {
        requireArguments().getString(KEY_TAB_NAME)
    }

    private val episodeId by lazy {
        requireArguments().getInt(KEY_EPISODE_ID)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodeDeatilsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setButtonBackClickListener()
        subscribeEpisodeDetailsFlow()
        notifyViewModel()
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated(episodeId)
    }

    private fun setAdapter() {
        binding.episodeDetailsRecyclerCharacters.adapter = characterListAdapter
    }

    private fun subscribeEpisodeDetailsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.episodeStateFlow
                    .onEach { episode ->
                        loadEpisode(episode)
                    }
                    .launchIn(this)

                viewModel.charactersListStateFlow
                    .onEach { characters ->
                        loadCharacters(characters)
                    }
                    .launchIn(this)

                viewModel.errorStateFlow
                    .onEach {
                        showError()
                    }
                    .launchIn(this)
            }
        }
    }

    private fun loadEpisode(episodeEntity: EpisodeEntity) {
        setEpisodeData(episodeEntity)
        checkLoadingCompleted()
    }

    private fun loadCharacters(characters: List<CharacterEntity>) {
        setCharactersData(characters)
        checkLoadingCompleted()
    }

    private fun setEpisodeData(episodeEntity: EpisodeEntity) {
        episode = episodeEntity
        binding.episodeDetailsNameTextView.text = episodeEntity.name
        binding.episodeDetailsAirDateText.text = episodeEntity.airDate
        binding.episodeDetailsCodeText.text = episodeEntity.episodeCode
    }

    private fun setCharactersData(characters: List<CharacterEntity>) {
        characterListAdapter.submitList(characters)
    }

    private fun showError() {
        hideContentViews()
        stopProgress()
        showErrorViews()
    }

    private fun hideContentViews() {
        binding.episodeDetailsNameTextView.isVisible = false
        binding.episodeDetailsCodeLabel.isVisible = false
        binding.episodeDetailsCodeText.isVisible = false
        binding.episodeDetailsAirDateText.isVisible = false
        binding.episodeDetailsAirDateLabel.isVisible = false
    }

    private fun showErrorViews() {
        binding.episodeDetailsErrorTextView.isVisible = true
        binding.episodeDetailsButtonReload.isVisible = true
        binding.episodeDetailsButtonReload.setOnClickListener {
            viewModel.onButtonReloadClick(episodeId)
            hideErrorViews()
            showContentViews()
            startProgress()
        }
    }

    private fun hideErrorViews() {
        binding.episodeDetailsErrorTextView.isVisible = false
        binding.episodeDetailsButtonReload.isVisible = false
    }

    private fun showContentViews() {
        binding.episodeDetailsNameTextView.isVisible = true
        binding.episodeDetailsCodeLabel.isVisible = true
        binding.episodeDetailsCodeText.isVisible = true
        binding.episodeDetailsAirDateText.isVisible = true
        binding.episodeDetailsAirDateLabel.isVisible = true
    }

    private fun checkLoadingCompleted() {
        loadedCount++
        if (loadedCount == COUNT_EXPECTED) {
            stopProgress()
        }
    }

    private fun startProgress() {
        binding.episodeDetailsProgressBar.isVisible = true
    }

    private fun stopProgress() {
        binding.episodeDetailsProgressBar.isVisible = false
    }

    private fun setButtonBackClickListener() {
        binding.episodeDetailsBackImageButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun launchCharacterDetailsFragment(id: Int) {
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, CharacterDetailsFragment.newInstance(id, it))
                .addToBackStack(it)
                .commit()
        }
    }

    companion object {
        private const val KEY_EPISODE_ID = "episodeId"
        private const val KEY_TAB_NAME = "tabName"
        private const val COUNT_START = 0
        private const val COUNT_EXPECTED = 2

        fun newInstance(id: Int, tabName: String) = EpisodeDetailsFragment().apply {
            arguments = bundleOf(KEY_EPISODE_ID to id, KEY_TAB_NAME to tabName)
        }
    }
}
