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
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.RickAndMortyApplication
import ru.example.rickandmortyproject.databinding.FragmentEpisodesFilterBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.utils.viewModelFactory

class EpisodesFilterFragment : BaseFragment() {

    private var _binding: FragmentEpisodesFilterBinding? = null

    private val binding get() = _binding!!
    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    private var restored: Boolean = false

    @Inject
    lateinit var viewModelProvider: Provider<EpisodesFilterViewModel>
    private val viewModel: EpisodesFilterViewModel by viewModelFactory {
        viewModelProvider.get()
    }

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
                .collect() { settings ->
                    if (!restored) {
                        setFilterSettings(settings)
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
            viewModel.onApplyClick(settings)
        }
    }

    private fun currentSettings(): EpisodeFilterSettings {
        val name = binding.episodesFilterNameEditText.text.toString()
        val code = binding.episodesFilterCodeEditText.text.toString()
        return EpisodeFilterSettings(name, code)
    }

    private fun setButtonBackListener() {
        binding.episodesFilterBackButton.setOnClickListener {
            RickAndMortyApplication.instance.router.exit()
        }
    }

    companion object {
        fun newInstance() = EpisodesFilterFragment()
    }
}
