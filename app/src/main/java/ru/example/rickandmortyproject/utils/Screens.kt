package ru.example.rickandmortyproject.utils

import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.example.rickandmortyproject.presentation.characters.details.CharacterDetailsFragment
import ru.example.rickandmortyproject.presentation.characters.list.CharactersFiltersFragment
import ru.example.rickandmortyproject.presentation.characters.list.CharactersListFragment
import ru.example.rickandmortyproject.presentation.episodes.details.EpisodeDetailsFragment
import ru.example.rickandmortyproject.presentation.episodes.list.EpisodesFilterFragment
import ru.example.rickandmortyproject.presentation.episodes.list.EpisodesListFragment
import ru.example.rickandmortyproject.presentation.locations.details.LocationDetailsFragment
import ru.example.rickandmortyproject.presentation.locations.list.LocationsFilterFragment
import ru.example.rickandmortyproject.presentation.locations.list.LocationsListFragment

object Screens {
    fun fragmentCharactersList(): Screen = FragmentScreen {
        CharactersListFragment.newInstance()
    }

    fun fragmentCharacterDetails(id: Int): Screen = FragmentScreen {
        CharacterDetailsFragment.newInstance(id)
    }

    fun fragmentCharacterFilters(): Screen = FragmentScreen {
        CharactersFiltersFragment.newInstance()
    }

    fun fragmentEpisodesList(): Screen = FragmentScreen {
        EpisodesListFragment.newInstance()
    }

    fun fragmentEpisodeDetails(id: Int): Screen = FragmentScreen {
        EpisodeDetailsFragment.newInstance(id)
    }

    fun fragmentEpisodeFilters(): Screen = FragmentScreen {
        EpisodesFilterFragment.newInstance()
    }

    fun fragmentLocationsList(): Screen = FragmentScreen {
        LocationsListFragment.newInstance()
    }

    fun fragmentLocationDetails(id: Int): Screen = FragmentScreen {
        LocationDetailsFragment.newInstance(id)
    }

    fun fragmentLocationFilters(): Screen = FragmentScreen {
        LocationsFilterFragment.newInstance()
    }
}
