package ru.example.rickandmortyproject.presentation.characters.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.presentation.characters.list.model.SingleCharacter

class CharacterListAdapter :
    RecyclerView.Adapter<CharacterListViewHolder>() {
    private val charactersList = mutableListOf<SingleCharacter>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        return CharacterListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_character,
                parent,
                false
            ),
        )
    }

    override fun getItemCount(): Int = charactersList.size

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        val character = charactersList[position]
        holder.bindCharacter(character)
    }
}