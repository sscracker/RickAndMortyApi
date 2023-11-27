package ru.example.rickandmortyproject.presentation.locations.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentLocationDetailsBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.characters.details.CharacterDetailsFragment
import ru.example.rickandmortyproject.presentation.characters.list.adapter.CharacterListAdapter
import ru.example.rickandmortyproject.utils.viewModelFactory

class LocationDetailsFragment : BaseFragment() {

    private var _binding: FragmentLocationDetailsBinding? = null

    private val binding get() = _binding!!

    private val residentsListAdapter by lazy {
        CharacterListAdapter(
            onListEnded = null,
            onItemClick = { character -> loadResidentsDetailsFragment(character.id) }
        )
    }

    private val tabName by lazy {
        requireArguments().getString(KEY_TAB_NAME)
    }

    private val locationId by lazy {
        requireArguments().getInt(KEY_LOCATION_ID)
    }

    @Inject
    internal lateinit var factory: LocationDetailsViewModel.Factory

    private val viewModel: LocationDetailsViewModel by viewModelFactory {
        factory.create(locationId)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startProgress()
        setAdapter()
        setButtonBackClickListener()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationsStateFlow
                    .onEach { location ->
                        showLocation(location)
                        stopProgress()
                    }
                    .launchIn(this)

                viewModel.residentsListStateFlow
                    .onEach { residents ->
                        showResidents(residents)
                    }
                    .launchIn(this)

                viewModel.errorStateFlow
                    .onEach {
                        showErrorBlock()
                    }
                    .launchIn(this)
            }
        }
    }

    private fun showErrorBlock() {
        hideContentViews()
        stopProgress()
        showErrorViews()
    }

    private fun hideContentViews() {
        binding.contentViewsGroup.isVisible = false
    }

    private fun showErrorViews() {
        binding.errorViewsGroup.isVisible = true
        binding.locationDetailsButtonReload.setOnClickListener {
            viewModel.onButtonReloadClick(locationId)
            hideErrorViews(false)
            showContentViews(true)
            startProgress()
        }
    }

    private fun hideErrorViews(show: Boolean) {
        binding.errorViewsGroup.isVisible = show
    }

    private fun showContentViews(show: Boolean) {
        binding.contentViewsGroup.isVisible = show
    }

    private fun startProgress() {
        binding.locationDetailsProgressBar.isVisible = true
    }

    private fun stopProgress() {
        binding.locationDetailsProgressBar.isVisible = false
    }

    private fun showLocation(locationEntity: LocationEntity) {
        setLocationData(locationEntity)
        stopProgress()
    }

    private fun setLocationData(locationEntity: LocationEntity) {
        viewModel.saveLocation(locationEntity)
        with(binding) {
            locationDetailsNameTextView.text = locationEntity.name
            locationDetailsTypeText.text = locationEntity.type
            locationDetailsDimensionText.text = locationEntity.dimension
        }
    }

    private fun showResidents(residents: List<CharacterEntity>) {
        setResidentsData(residents)
        stopProgress()
    }

    private fun setResidentsData(residents: List<CharacterEntity>) {
        residentsListAdapter.submitList(residents)
    }

    private fun setAdapter() {
        binding.locationDetailsRecyclerCharacters.adapter = residentsListAdapter
    }

    private fun setButtonBackClickListener() {
        binding.locationDetailsBackImageButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun loadResidentsDetailsFragment(residentId: Int) {
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.fragment_container,
                    CharacterDetailsFragment.newInstance(residentId, it)
                )
                .addToBackStack(it)
                .commit()
        }
    }

    companion object {
        private const val KEY_TAB_NAME = "tabName"
        private const val KEY_LOCATION_ID = "episodeId"

        fun newInstance(id: Int, tabName: String) = LocationDetailsFragment().apply {
            arguments = bundleOf(KEY_LOCATION_ID to id, KEY_TAB_NAME to tabName)
        }
    }
}
