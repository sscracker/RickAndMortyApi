package ru.example.rickandmortyproject.presentation.characters.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.data.characters.list.model.SingleCharacterEntity
import ru.example.rickandmortyproject.presentation.characters.list.model.SingleCharacter

class CharacterListAdapter :
    RecyclerView.Adapter<CharacterListViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<SingleCharacter>(){
        override fun areItemsTheSame(oldItem: SingleCharacter, newItem: SingleCharacter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SingleCharacter,
            newItem: SingleCharacter
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        return CharacterListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_character,
                parent,
                false
            ),
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        val character = differ.currentList[position]
        holder.bindCharacter(character)
    }

}