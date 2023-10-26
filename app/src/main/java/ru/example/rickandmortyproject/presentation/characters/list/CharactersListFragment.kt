package ru.example.rickandmortyproject.presentation.characters.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.databinding.FragmentCharactersBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.presentation.base.BaseFragment
import ru.example.rickandmortyproject.presentation.characters.list.adapter.CharacterListAdapter
import ru.example.rickandmortyproject.presentation.characters.list.model.SingleCharacter
import ru.example.rickandmortyproject.utils.ViewState

private const val COLUMN_COUNT = 2

class CharactersListFragment :
    BaseFragment<CharacterListViewModel>(CharacterListViewModel::class.java) {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private var characterAdapter = CharacterListAdapter()

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
        observeViewModel()
        updateCharacters()
    }

    private fun initCharacterList() {
        with(binding.recyclerViewCharacters) {
            layoutManager = GridLayoutManager(requireActivity(), COLUMN_COUNT)
            adapter = characterAdapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllCharacters().collect { viewState ->
                    updateCharactersState(viewState)
                }
            }
        }
    }

    private fun updateCharactersState(viewState: ViewState<List<SingleCharacter>>) {
        when (viewState) {
            is ViewState.Loading -> loadingState()
            is ViewState.Error -> errorState(viewState.error.message.orEmpty())
            is ViewState.Data -> dataState(viewState.data)
        }
    }

    private fun dataState(list: List<SingleCharacter>) {
        characterAdapter.differ.submitList(list)
        binding.progressBar.isVisible = false
    }


    private fun errorState(message: String) {
        binding.progressBar.isVisible = false
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun loadingState() {
        binding.progressBar.isVisible = true
    }

    private fun updateCharacters() {
        with(binding.swipeRefreshLayoutCharacterList) {
            setOnRefreshListener {
                viewModel.loadCharacters()
                this.isRefreshing = true
            }
        }
    }

    companion object {
        const val CHARACTERS_TAG = "CHARACTERS"
        fun newInstance() = CharactersListFragment()
    }
}