package ru.example.rickandmortyproject.presentation.characters.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import ru.example.rickandmortyproject.MainActivity
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentCharactersBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.presentation.characters.list.adapter.CharacterListAdapter
import ru.example.rickandmortyproject.presentation.characters.list.model.SingleCharacter
import ru.example.rickandmortyproject.utils.ViewModelFactory
import ru.example.rickandmortyproject.utils.ViewState
import javax.inject.Inject

private const val COLUMN_COUNT = 2

class CharactersListFragment : Fragment(R.layout.fragment_characters) {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private var characterAdapter: CharacterListAdapter? = null
    private var appComponent: AppComponent? = null
    @Inject
    lateinit var viewModel: CharacterListViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent = (activity as MainActivity).appComponent
        appComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = appComponent!!.getViewModelFactory()
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

        viewModel = ViewModelProvider(this, viewModelFactory)[CharacterListViewModel::class.java]
        observeViewModel()
        updateCharacters()
        initCharacterList()
    }

    private fun initCharacterList() {
        characterAdapter = CharacterListAdapter()
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

    private fun dataState(data: List<SingleCharacter>) {
        characterAdapter?.updateCharacterList(data)
        binding.progressBar.visibility = View.GONE
    }

    private fun errorState(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun loadingState() {
        binding.progressBar.visibility = View.VISIBLE
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