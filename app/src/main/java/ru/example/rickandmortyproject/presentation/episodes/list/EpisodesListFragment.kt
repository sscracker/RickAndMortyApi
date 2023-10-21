package ru.example.rickandmortyproject.presentation.episodes.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.databinding.FragmentEpisodesBinding
import ru.example.rickandmortyproject.presentation.episodes.list.adapter.EpisodesListAdapter

private const val COLUMN_COUNT = 2

class EpisodesListFragment : Fragment(R.layout.fragment_episodes) {
    private var _binding: FragmentEpisodesBinding? = null
    private val binding get() = _binding!!

    private val tabName by lazy {
        requireArguments().getString(KEY_TAB_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEpisodesList()
    }

    private fun initEpisodesList() {
        with(binding.recyclerViewEpisodes) {
            layoutManager = GridLayoutManager(requireActivity(), COLUMN_COUNT)
            adapter = EpisodesListAdapter()
        }
    }

    companion object {
        private const val KEY_TAB_NAME = "tabName"

        fun newInstance(tabName: String) = EpisodesListFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}