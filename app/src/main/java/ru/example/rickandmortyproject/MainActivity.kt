package ru.example.rickandmortyproject

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.navigation.NavigationBarView
import ru.example.rickandmortyproject.databinding.ActivityMainBinding
import ru.example.rickandmortyproject.utils.Screens

class MainActivity : FragmentActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navigator = AppNavigator(this, R.id.fragment_container)

    private val navigationListener = NavigationBarView.OnItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.menu_item_characters -> {
                RickAndMortyApplication.instance.router.navigateTo(
                    Screens.fragmentCharactersList()
                )
            }
            R.id.menu_item_episodes -> {
                RickAndMortyApplication.instance.router.navigateTo(
                    Screens.fragmentEpisodesList()
                )
            }
            R.id.menu_item_locations -> {
                RickAndMortyApplication.instance.router.navigateTo(
                    Screens.fragmentLocationsList()
                )
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setNavigationListener()
        setDefaultSelectedTab(savedInstanceState)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        RickAndMortyApplication.instance.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        RickAndMortyApplication.instance.navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun setNavigationListener() {
        binding.mainNavigation.setOnItemSelectedListener(navigationListener)
    }

    private fun setDefaultSelectedTab(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding.mainNavigation.selectedItemId = R.id.menu_item_characters
        }
    }

    companion object {
        private const val TAB_NAME_CHARACTERS = "tabCharacters"
        private const val TAB_NAME_EPISODES = "tabEpisodes"
        private const val TAB_NAME_LOCATIONS = "tabLocations"
    }
}
