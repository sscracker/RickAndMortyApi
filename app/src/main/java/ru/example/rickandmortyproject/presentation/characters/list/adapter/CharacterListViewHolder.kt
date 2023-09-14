package ru.example.rickandmortyproject.presentation.characters.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.example.rickandmortyproject.databinding.ItemCharacterBinding
import ru.example.rickandmortyproject.presentation.characters.list.SingleCharacter

class CharacterListViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {
    private val binding: ItemCharacterBinding = ItemCharacterBinding.bind(itemView)

    fun bindCharacter(
        singleCharacter: SingleCharacter
    ) {
        with(binding) {
            textViewCharacterName.text = singleCharacter.name
            textViewCharacterGender.text = singleCharacter.gender
            textViewCharacterSpecies.text = singleCharacter.species
            textViewCharacterStatus.text = singleCharacter.status
        }
        Glide.with(itemView.context)
            .load(singleCharacter.image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imageViewCharacterPhoto)
    }
}