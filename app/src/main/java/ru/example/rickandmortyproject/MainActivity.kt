package ru.example.rickandmortyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.example.rickandmortyproject.presentation.characters.list.CharactersListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().run {
            setReorderingAllowed(true)
            val charactersListFragment = CharactersListFragment.newInstance()
            replace(R.id.fragment_container,charactersListFragment, CharactersListFragment.CHARACTERS_TAG)
            addToBackStack(CharactersListFragment.CHARACTERS_TAG)
            commit()
        }
    }


}