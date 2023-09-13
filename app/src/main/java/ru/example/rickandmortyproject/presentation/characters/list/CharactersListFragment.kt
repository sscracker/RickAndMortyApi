package ru.example.rickandmortyproject.presentation.characters.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentCharactersBinding
import ru.example.rickandmortyproject.presentation.characters.CharacterListDetailsListener
import ru.example.rickandmortyproject.presentation.characters.list.adapter.CharactersListAdapter

class CharactersListFragment : Fragment(R.layout.fragment_characters) {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private lateinit var characterAdapter: CharactersListAdapter

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
        characterAdapter = CharactersListAdapter()
        with(binding.charactersRv) {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = adapter
        }
    }
    companion object {
        const val CHARACTERS_TAG = "CHARACTERS"
        fun newInstance() = CharactersListFragment()
    }
}