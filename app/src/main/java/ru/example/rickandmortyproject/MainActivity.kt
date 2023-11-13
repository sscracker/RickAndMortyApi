package ru.example.rickandmortyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import ru.example.rickandmortyproject.databinding.ActivityMainBinding
import ru.example.rickandmortyproject.presentation.characters.list.CharactersListFragment
import ru.example.rickandmortyproject.presentation.episodes.list.EpisodesListFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navigationListener = NavigationBarView.OnItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.menu_item_characters -> {
                val startFragment = CharactersListFragment.newInstance(TAB_NAME_CHARACTERS)
                showTab(startFragment, TAB_NAME_CHARACTERS)
            }

            R.id.menu_item_episodes -> {
                val startFragment = EpisodesListFragment.newInstance(TAB_NAME_EPISODES)
                showTab(startFragment, TAB_NAME_EPISODES)
            }
        }
        true
    }

    private var selectedTabName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setNavigationListener()
        setDefaultSelectedTab(savedInstanceState)
    }

    private fun showTab(startFragment: Fragment, tabName: String) {
        selectedTabName?.let {
            supportFragmentManager.saveBackStack(it)
        }
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragment_container, startFragment)
            .commit()
        supportFragmentManager.restoreBackStack(tabName)
        selectedTabName = tabName
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
    }
}