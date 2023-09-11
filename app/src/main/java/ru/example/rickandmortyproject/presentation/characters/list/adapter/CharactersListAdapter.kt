package ru.example.rickandmortyproject.presentation.characters.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.presentation.characters.CharacterListDetailsListener
import ru.example.rickandmortyproject.presentation.characters.list.SingleCharacter

class CharactersListAdapter/*(private val characterListener: CharacterListDetailsListener)*/:RecyclerView.Adapter<CharactersListViewHolder>()
{
    private val charactersList = mutableListOf<SingleCharacter>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersListViewHolder {
        return CharactersListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_character,
                parent,
                false
            ),
        )
    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    override fun onBindViewHolder(holder: CharactersListViewHolder, position: Int) {
        val character = charactersList[position]
        holder.bindCharacter(character)
    }

}