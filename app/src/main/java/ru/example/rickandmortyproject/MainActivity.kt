package ru.example.rickandmortyproject

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ru.example.rickandmortyproject.presentation.characters.list.CharactersListFragment

class MainActivity : FragmentActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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