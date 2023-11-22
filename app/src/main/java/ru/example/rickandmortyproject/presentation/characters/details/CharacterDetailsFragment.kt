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
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentCharactersDetailsBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterGender
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterStatus
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.utils.viewModelFactory

class CharacterDetailsFragment : BaseFragment() {

    private var _binding: FragmentCharactersDetailsBinding? = null
    private val binding get() = _binding!!

    private var loadedCount = COUNT_START

    private var characterEntity: CharacterEntity? = null

    private val tabName by lazy {
        requireArguments().getString(KEY_TAB_NAME)
    }

    private val characterId by lazy {
        requireArguments().getInt(KEY_CHARACTER_ID)
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
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonBackListener()
        subscribeFlow()

        if (loadedCount == COUNT_START) {
            startProgress()
        }
    }

    private fun setButtonBackListener() {
        binding.characterDetailsBackImageButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun subscribeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.characterState
                    .onEach { character ->
                        loadCharacter(character)
                    }
                    .launchIn(this)
                viewModel.errorState
                    .onEach {
                        showError()
                    }
                    .launchIn(this)
            }
        }
    }

    private fun loadCharacter(character: CharacterEntity) {
        setCharacterData(character)
        checkLoadingCompleted()
    }

    private fun setCharacterData(character: CharacterEntity) {
        characterEntity = character
        binding.characterDetailsNameTextView.text = character.name
        binding.characterDetailsStatusTextView.text = when (character.status) {
            CharacterStatus.ALIVE -> getString(R.string.status_alive)
            CharacterStatus.DEAD -> getString(R.string.status_dead)
            CharacterStatus.UNKNOWN -> getString(R.string.status_unknown)
        }
        binding.characterDetailsSpeciesTextView.text = character.species
        binding.characterDetailsTypeTextView.text = character.type

        binding.characterDetailsGenderTextView.text = when (character.gender) {
            CharacterGender.FEMALE -> getString(R.string.gender_female)
            CharacterGender.MALE -> getString(R.string.gender_male)
            CharacterGender.GENDERLESS -> getString(R.string.gender_genderless)
            CharacterGender.UNKNOWN -> getString(R.string.status_unknown)
        }
        binding.characterDetailsPhotoImageView.load(character.image) {
            error(R.drawable.person_placeholder)
        }
    }

    private fun checkLoadingCompleted() {
        loadedCount++
        if (loadedCount == 1) {
            stopProgress()
        }
    }

    private fun showError() {
        hideViews()
        stopProgress()
        showErrorViews()
    }

    private fun showErrorViews() {
        binding.characterDetailsErrorTextView.isVisible = true
        binding.characterDetailsButtonReload.isVisible = true
        binding.characterDetailsButtonReload.setOnClickListener {
            viewModel.onButtonReloadPressed(characterId)
            hideErrorView()
            showContent()
            startProgress()
        }
    }

    private fun hideErrorView() {
        binding.characterDetailsErrorTextView.isVisible = false
        binding.characterDetailsButtonReload.isVisible = false
    }

    private fun showContent() {
        binding.characterDetailsNameTextView.isVisible = true
        binding.characterDetailsScrollView.isVisible = true
    }

    private fun hideViews() {
        binding.characterDetailsNameTextView.isVisible = false
        binding.characterDetailsScrollView.isVisible = false
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

        fun newInstance(id: Int, tabName: String) = CharacterDetailsFragment().apply {
            arguments = bundleOf(KEY_CHARACTER_ID to id, KEY_TAB_NAME to tabName)
        }
    }
}
