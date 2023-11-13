package ru.example.rickandmortyproject.presentation.characters.list

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
import ru.example.rickandmortyproject.databinding.FragmentCharactersBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.characters.details.CharacterDetailsFragment
import ru.example.rickandmortyproject.presentation.characters.list.adapter.CharacterListAdapter
import ru.example.rickandmortyproject.utils.showToast

private const val COLUMN_COUNT = 2

class CharactersListFragment :
    BaseFragment<CharacterListViewModel>(CharacterListViewModel::class.java) {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val characterAdapter by lazy {
        CharacterListAdapter(onListEnded =
        {
            viewModel.onListEnded()
            startProgress()
        },
            onItemClick = { character ->
                launchDetailsFragment(character.id)
            })
    }

    private val tabName by lazy {
        requireArguments().getString(KEY_TAB_NAME)
    }

    private fun launchDetailsFragment(id: Int) {
        tabName?.let { characterDetailsTabName ->
            parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(
                    R.id.fragment_container,
                    CharacterDetailsFragment.newInstance(id, characterDetailsTabName)
                )
                .addToBackStack(characterDetailsTabName).commit()
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCharacterList()
        configSwipeLayout()
        setOnRefreshListener()
        setButtonFilterListener()
        setFilterChangedResultListener()
        setSearchViewListener()
        subscribeCharactersFlow()
        notifyViewModel()
        startProgress()
    }

    private fun initCharacterList() {
        with(binding.recyclerViewCharacters) {
            layoutManager = GridLayoutManager(requireActivity(), COLUMN_COUNT)
            adapter = characterAdapter
        }
    }

    private fun subscribeCharactersFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.charactersListState.onEach {
                    processCharactersList(it)
                }.launchIn(this)

                viewModel.notEmptyFilterState.onEach {
                    setButtonClearState(it)
                }.launchIn(this)

                viewModel.errorState.onEach {
                    showError()
                    stopProgress()
                }.launchIn(this)

                viewModel.emptyResultState.onEach {
                    showEmptyResult()
                }.launchIn(this)
            }
        }
    }

    private fun setButtonClearState(notEmptyFilter: Boolean) {
        if (notEmptyFilter) {
            binding.charactersButtonClear.setBackgroundResource(R.drawable.app_rectangle_button)
            binding.charactersButtonClear.setOnClickListener {
                viewModel.onButtonClearPressed()
                startProgress()
            }
        } else {
            binding.charactersButtonClear.setBackgroundResource(R.drawable.app_gray_button)
            binding.charactersButtonClear.setOnClickListener(null)
        }
    }

    private fun processCharactersList(characters: List<CharacterEntity>) {
        characterAdapter.submitList(characters)
        stopProgress()
    }

    private fun configSwipeLayout() {
        binding.swipeRefreshLayoutCharacterList.setProgressViewEndTarget(false, 0)
    }

    private fun setOnRefreshListener() {
        binding.swipeRefreshLayoutCharacterList.setOnRefreshListener {
            viewModel.onListSwiped()
            startProgress()
        }
    }

    private fun setButtonFilterListener() {
        binding.charactersButtonFilter.setOnClickListener {
            launchFilterFragment()
        }
    }

    private fun setFilterChangedResultListener() {
        setFragmentResultListener(KEY_FILTER_CHANGED) { key, bundle ->
            val isChanged = bundle.getBoolean(key)
            if (isChanged) {
                viewModel.onFilterSettingsChanged()
            }
        }
    }

    private fun launchFilterFragment() {
        tabName?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, CharactersFiltersFragment.newInstance())
                .addToBackStack(it)
                .commit()
        }
    }

    private fun setSearchViewListener() {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(changedText: String?): Boolean {
                viewModel.onSearchQueryChanged(changedText)
                return true
            }
        }
        binding.charactersSearchView.setOnQueryTextListener(listener)
    }

    private fun showError() {
        val errorMessage = "Error! Pull the list and retry loading"
        requireContext().showToast(errorMessage)
    }

    private fun showEmptyResult() {
        val emptyMessage = "Results not found"
        requireContext().showToast(emptyMessage)
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    private fun startProgress() {
        binding.progressBar.isVisible = true
    }

    private fun stopProgress() {
        binding.progressBar.isVisible = false
    }

    companion object {
        const val KEY_FILTER_CHANGED = "charactersFilterChanged"
        private const val KEY_TAB_NAME = "tabName"
        fun newInstance(tabName: String) = CharactersListFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }

}