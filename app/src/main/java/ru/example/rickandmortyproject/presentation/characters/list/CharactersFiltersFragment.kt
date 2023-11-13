package ru.example.rickandmortyproject.presentation.characters.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.databinding.FragmentFilterCharactersBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterFilterSettings
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterGender
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterStatus
import ru.example.rickandmortyproject.presentation.base.BaseFragment

class CharactersFiltersFragment :
    BaseFragment<CharactersFiltersViewModel>(CharactersFiltersViewModel::class.java) {

    private var _binding: FragmentFilterCharactersBinding? = null
    private val binding get() = _binding!!

    private var restored: Boolean = false
    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterCharactersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreState(savedInstanceState)
        setButtonBackListener()
        setButtonApplyListener()
        subscribeFilterSettingsFlow()
        subscribeFilterSavedFlow()
        notifyViewModel()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val statusPosition = binding.charactersFilterStatusSpinner.selectedItemPosition
        val genderPosition = binding.charactersFilterGenderSpinner.selectedItemPosition
        outState.putInt(KEY_STATUS_POSITION, statusPosition)
        outState.putInt(KEY_GENDER_POSITION, genderPosition)
    }

    private fun restoreState(savedInstanceState: Bundle?){
        savedInstanceState?.let {
            val statusPosition = it.getInt(KEY_STATUS_POSITION)
            val genderPosition = it.getInt(KEY_GENDER_POSITION)
            binding.charactersFilterGenderSpinner.setSelection(genderPosition)
            binding.charactersFilterStatusSpinner.setSelection(statusPosition)
            restored = true
        }
    }

    private fun setButtonBackListener(){
        binding.charactersFilterBackButton.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setButtonApplyListener() {
        binding.charactersFilterApplyButton.setOnClickListener {
            val settings = currentSettings()
            viewModel.onApplyPressed(settings)
        }
    }

    private fun currentSettings(): CharacterFilterSettings {
        val name = binding.charactersFilterNameEditText.text.toString()
        val status = when(val statusPosition = binding.charactersFilterStatusSpinner.selectedItemPosition){
            0 -> null
            1 -> CharacterStatus.ALIVE
            2 -> CharacterStatus.DEAD
            3 -> CharacterStatus.UNKNOWN
            else -> throw RuntimeException("Unknown spinner position: ${statusPosition} ")
        }
        val species = binding.charactersFilterSpeciesEditText.text.toString()
        val type = binding.charactersFilterTypeEditText.text.toString()
        val gender = when(val genderPosition = binding.charactersFilterGenderSpinner.selectedItemPosition){
            0 -> null
            1 -> CharacterGender.FEMALE
            2 -> CharacterGender.MALE
            3 -> CharacterGender.GENDERLESS
            4 -> CharacterGender.UNKNOWN
            else -> throw RuntimeException("Unknown spinner position: ${genderPosition}")
        }
        return CharacterFilterSettings(name, status, species, type, gender)
    }

    private fun subscribeFilterSettingsFlow(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.charactersFilterState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect{
                    if (!restored){
                        setFilterSettings(it)
                    }
                }
        }
    }

    private fun setFilterSettings(settings: CharacterFilterSettings) {
        binding.charactersFilterNameEditText.setText(settings.name)
        binding.charactersFilterStatusSpinner.setSelection(
            when(settings.status){
                null -> 0
                CharacterStatus.ALIVE -> 1
                CharacterStatus.DEAD -> 2
                CharacterStatus.UNKNOWN -> 3
            }
        )
        binding.charactersFilterSpeciesEditText.setText(settings.species)
        binding.charactersFilterTypeEditText.setText(settings.type)
        binding.charactersFilterGenderSpinner.setSelection(
            when(settings.gender){
                null -> 0
                CharacterGender.FEMALE -> 1
                CharacterGender.MALE -> 2
                CharacterGender.GENDERLESS -> 3
                CharacterGender.UNKNOWN -> 4
            }
        )
    }

    private fun subscribeFilterSavedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filterSavedState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect{
                    saveFilterSettings()
                }
        }
    }

    private fun saveFilterSettings() {
        setFragmentResult(
            CharactersListFragment.KEY_FILTER_CHANGED,
            bundleOf(CharactersListFragment.KEY_FILTER_CHANGED to true)
        )
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun notifyViewModel(){
        viewModel.onViewCreated()
    }

    companion object{
        private const val KEY_STATUS_POSITION = "statusPosition"
        private const val KEY_GENDER_POSITION = "genderPosition"

        fun newInstance() = CharactersFiltersFragment()
    }
}