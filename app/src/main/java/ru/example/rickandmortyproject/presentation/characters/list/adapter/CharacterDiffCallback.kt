package ru.example.rickandmortyproject.presentation.characters.list.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity

object CharacterDiffCallback: DiffUtil.ItemCallback<CharacterEntity>() {
    override fun areItemsTheSame(
        oldItem: CharacterEntity,
        newItem: CharacterEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharacterEntity,
        newItem: CharacterEntity
    ): Boolean {
        return oldItem == newItem
    }
}