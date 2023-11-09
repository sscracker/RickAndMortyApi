package ru.example.rickandmortyproject.presentation.locations.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.example.rickandmortyproject.databinding.FragmentLocationsBinding
import ru.example.rickandmortyproject.presentation.locations.list.adapter.LocationsListAdapter

class LocationsListFragment : Fragment() {
    private var _binding: FragmentLocationsBinding? = null

    private val binding get() = _binding!!

    private val locationsAdapter by lazy {
        LocationsListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        binding.locationsListRecyclerView.adapter = locationsAdapter
    }

    companion object {
        private const val KEY_TAB_NAME = "tabName"

        fun newInstance(tabName: String) = LocationsListFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}
