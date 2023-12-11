package ru.example.rickandmortyproject.presentation.characters.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentCharactersDetailsBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterGender
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterStatus
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.episodes.details.EpisodeDetailsFragment
import ru.example.rickandmortyproject.presentation.episodes.list.adapter.EpisodesListAdapter
import ru.example.rickandmortyproject.presentation.locations.details.LocationDetailsFragment
import ru.example.rickandmortyproject.utils.showToast
import ru.example.rickandmortyproject.utils.viewModelFactory
import javax.inject.Inject

class CharacterDetailsFragment : BaseFragment() {
    private var _binding: FragmentCharactersDetailsBinding? = null
    private val binding get() = _binding!!

    private var loadedCount = COUNT_START

    var characterEntity: CharacterEntity? = null

    private val tabName by lazy {
        requireArguments().getString(KEY_TAB_NAME)
    }

    private val characterId by lazy {
        requireArguments().getInt(KEY_CHARACTER_ID)
    }

    val adapter by lazy {
        EpisodesListAdapter(
            null,
            onItemClick = { episode ->
                launchEpisodeDetailsFragment(episode.id)
            },
        )
    }

    @Inject
    internal lateinit var factory: CharacterDetailsViewModel.Factory

    private val viewModel: CharacterDetailsViewModel by viewModelFactory {
        factory.create(characterId)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCharactersDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setButtonBackListener()
        setOriginAndLocationListeners()
        setEpisodesAdapter()
        observeData()
        showContentViews(true)

        if (loadedCount == COUNT_START) {
            startProgress()
        }
    }

    private fun setEpisodesAdapter() {
        binding.characterDetailsRecyclerViewEpisodes.adapter = adapter
    }

    private fun setOriginAndLocationListeners() {
        with(binding) {
            characterDetailsLocationCardView.setOnClickListener {
                characterEntity?.let { character ->
                    launchLocationDetailsFragment(character.originId)
                }
            }
            characterDetailsOriginCardView.setOnClickListener {
                characterEntity?.let { character ->
                    launchLocationDetailsFragment(character.locationId)
                }
            }
        }
    }

    private fun launchLocationDetailsFragment(id: Int) {
        if (id == UNDEFINED_ID) {
            requireContext().showToast(requireContext().getString(R.string.unknown_location))
            return
        }
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, LocationDetailsFragment.newInstance(id, it))
                .addToBackStack(it)
                .commit()
        }
    }

    private fun launchEpisodeDetailsFragment(id: Int) {
        if (id == UNDEFINED_ID) {
            requireContext().showToast(requireContext().getString(R.string.unknown_episode))
            return
        }
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, EpisodeDetailsFragment.newInstance(id, it))
                .addToBackStack(it)
                .commit()
        }
    }

    private fun setButtonBackListener() {
        binding.characterDetailsBackImageButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.characterStateFlow
                    .onEach { character ->
                        loadCharacter(character)
                    }
                    .launchIn(this)

                viewModel.originStateFlow
                    .onEach { origin ->
                        loadOrigin(origin)
                    }
                    .launchIn(this)

                viewModel.locationStateFlow
                    .onEach { location ->
                        loadLocation(location)
                    }
                    .launchIn(this)

                viewModel.episodesListStateFlow
                    .onEach { episodesList ->
                        loadEpisodesList(episodesList)
                    }
                    .launchIn(this)

                viewModel.errorStateFlow
                    .onEach {
                        showError(true)
                        reload()
                    }
                    .launchIn(this)
            }
        }
    }

    private fun loadOrigin(originName: String) {
        setOrigin(originName)
        stopProgress()
    }

    private fun setOrigin(originName: String) {
        binding.characterDetailsOriginTextView.text =
            String.format(
                FORMAT,
                getString(R.string.origin_label),
                originName,
            )
    }

    private fun loadLocation(locationName: String) {
        setLocation(locationName)
        stopProgress()
    }

    private fun setLocation(locationName: String) {
        binding.characterDetailsLocationTextView.text =
            String.format(
                FORMAT,
                getString(R.string.location_label),
                locationName,
            )
    }

    private fun loadEpisodesList(episodes: List<EpisodeEntity>) {
        adapter.submitList(episodes)
    }

    private fun loadCharacter(character: CharacterEntity) {
        showCharacterData(character)
        stopProgress()
    }

    private fun showCharacterData(character: CharacterEntity) {
        characterEntity = character
        binding.characterDetailsNameTextView.text = character.name
        binding.characterDetailsStatusTextView.text =
            when (character.status) {
                CharacterStatus.ALIVE -> getString(R.string.status_alive)
                CharacterStatus.DEAD -> getString(R.string.status_dead)
                CharacterStatus.UNKNOWN -> getString(R.string.status_unknown)
            }
        binding.characterDetailsSpeciesTextView.text = character.species
        binding.characterDetailsTypeTextView.text = character.type

        binding.characterDetailsGenderTextView.text =
            when (character.gender) {
                CharacterGender.FEMALE -> getString(R.string.gender_female)
                CharacterGender.MALE -> getString(R.string.gender_male)
                CharacterGender.GENDERLESS -> getString(R.string.gender_genderless)
                CharacterGender.UNKNOWN -> getString(R.string.status_unknown)
            }
        binding.characterDetailsPhotoImageView.load(character.image) {
            error(R.drawable.person_placeholder)
        }
    }

    private fun showError(show: Boolean) {
        showContentViews(!show)
        binding.errorGroup.isVisible = show
    }

    private fun reload() {
        binding.characterDetailsButtonReload.setOnClickListener {
            viewModel.onButtonReloadPressed(characterId)
        }
    }

    private fun showContentViews(show: Boolean) {
        binding.contentGroup.isVisible = show
    }

    private fun startProgress() {
        binding.characterDetailsProgressBar.isVisible = true
    }

    private fun stopProgress() {
        binding.characterDetailsProgressBar.isVisible = false
    }

    companion object {
        private const val KEY_TAB_NAME = "tabName"
        private const val KEY_CHARACTER_ID = "characterId"
        private const val COUNT_START = 0
        private const val UNDEFINED_ID = -1
        private const val FORMAT = "%s: %s"

        fun newInstance(
            id: Int,
            tabName: String,
        ) = CharacterDetailsFragment().apply {
            arguments = bundleOf(KEY_CHARACTER_ID to id, KEY_TAB_NAME to tabName)
        }
    }
}
