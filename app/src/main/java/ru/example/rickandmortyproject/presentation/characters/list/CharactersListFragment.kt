package ru.example.rickandmortyproject.presentation.characters.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentCharactersBinding
import ru.example.rickandmortyproject.presentation.characters.list.adapter.CharacterListAdapter

private const val COLUMN_COUNT = 2
class CharactersListFragment : Fragment(R.layout.fragment_characters) {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private var characterAdapter: CharacterListAdapter? = null

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
        characterAdapter = CharacterListAdapter()
        with(binding.recyclerViewCharacters) {
            layoutManager = GridLayoutManager(requireActivity(), COLUMN_COUNT)
            adapter = characterAdapter
        }
    }

    companion object {
        const val CHARACTERS_TAG = "CHARACTERS"
        fun newInstance() = CharactersListFragment()
    }
}