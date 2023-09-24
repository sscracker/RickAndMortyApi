package ru.example.rickandmortyproject

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ru.example.rickandmortyproject.databinding.ActivityMainBinding
import ru.example.rickandmortyproject.di.AppComponent
import ru.example.rickandmortyproject.di.DaggerAppComponent
import ru.example.rickandmortyproject.presentation.characters.list.CharactersListFragment

class MainActivity : FragmentActivity(R.layout.activity_main) {

    private var binding: ActivityMainBinding? = null
    var appComponent: AppComponent = DaggerAppComponent.factory().create(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportFragmentManager.beginTransaction().run {
            setReorderingAllowed(true)
            val charactersListFragment = CharactersListFragment.newInstance()
            replace(
                R.id.fragment_container,
                charactersListFragment,
                CharactersListFragment.CHARACTERS_TAG
            )
            addToBackStack(CharactersListFragment.CHARACTERS_TAG)
            commit()
        }
    }
}