package ru.example.rickandmortyproject.presentation.characters.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.example.rickandmortyproject.databinding.ItemCharacterBinding
import ru.example.rickandmortyproject.presentation.characters.CharacterListDetailsListener
import ru.example.rickandmortyproject.presentation.characters.list.SingleCharacter

class CharactersListViewHolder(
    itemView: View,
    /*private val characterListDetailsListener: CharacterListDetailsListener*/
):RecyclerView.ViewHolder(itemView) {
    private val binding: ItemCharacterBinding = ItemCharacterBinding.bind(itemView)


    fun bindCharacter(
        singleCharacter: SingleCharacter
    ){
        with(binding){
            characterName.text = singleCharacter.name
            characterGender.text = singleCharacter.gender
            characterSpecies.text = singleCharacter.species
            characterStatus.text = singleCharacter.status
        }
        Glide.with(itemView.context)
            .load(singleCharacter.image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imageCharacter)

       /* itemView.setOnClickListener{
            characterListDetailsListener.charactersListener(singleCharacter)
        }*/

    }
}