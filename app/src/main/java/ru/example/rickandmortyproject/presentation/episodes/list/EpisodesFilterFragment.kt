package ru.example.rickandmortyproject.presentation.episodes.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.databinding.FragmentEpisodesFilterBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings
import ru.example.rickandmortyproject.presentation.base.BaseFragment

class EpisodesFilterFragment :
    BaseFragment<EpisodesFilterViewModel>(EpisodesFilterViewModel::class.java) {

    private var _binding: FragmentEpisodesFilterBinding? = null

    private val binding get() = _binding!!
    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    private var restored: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodesFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonBackListener()
        setButtonApplyListener()
        subscribeFilterSettingsFlow()
        subscribeOnFilterSavedFlow()
        notifyViewModel()
    }

    private fun subscribeOnFilterSavedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveFilterStateFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    saveFilterSettings()
                }
        }
    }

    private fun saveFilterSettings() {
        setFragmentResult(
            EpisodesListFragment.KEY_FILTER_CHANGED,
            bundleOf(EpisodesListFragment.KEY_FILTER_CHANGED to true)
        )
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun subscribeFilterSettingsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getEpisodesFilterStateFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect() {
                    if (!restored) {
                        setFilterSettings(it)
                    }
                }
        }
    }

    private fun setFilterSettings(settings: EpisodeFilterSettings) {
        binding.episodesFilterNameEditText.setText(settings.name)
        binding.episodesFilterCodeEditText.setText(settings.code)
    }

    private fun setButtonApplyListener() {
        binding.episodesFilterApplyButton.setOnClickListener {
            val settings = currentSettings()
            viewModel.onApplyPressed(settings)
        }
    }

    private fun currentSettings(): EpisodeFilterSettings {
        val name = binding.episodesFilterNameEditText.text.toString()
        val code = binding.episodesFilterCodeEditText.text.toString()
        return EpisodeFilterSettings(name, code)
    }

    private fun setButtonBackListener() {
        binding.episodesFilterBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    companion object {
        fun newInstance() = EpisodesFilterFragment()
    }
}
