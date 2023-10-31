package ru.example.rickandmortyproject.presentation.characters.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterEntity
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterGender
import ru.example.rickandmortyproject.domain.characters.list.model.CharacterStatus

class CharacterListAdapter(
    private val onListEnded: (() -> Unit)?,
    private val onItemClick: (CharacterEntity) -> Unit
) : ListAdapter<CharacterEntity, CharacterListAdapter.CharacterListViewHolder>(
    CharacterDiffCallback
) {
    class CharacterListViewHolder(itemView: View) : ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.item_image_view_character_photo)
        val name: TextView = itemView.findViewById(R.id.item_text_view_character_name)
        val species: TextView = itemView.findViewById(R.id.item_text_view_character_species)
        val status: TextView = itemView.findViewById(R.id.item_text_view_character_status)
        val gender: TextView = itemView.findViewById(R.id.item_text_view_character_gender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
            .let {
                CharacterListViewHolder(it)
            }

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        val entity = getItem(position)
        holder.name.text = entity.name
        holder.species.text = entity.species

        holder.status.text = when (entity.status) {
            CharacterStatus.ALIVE -> STATUS_ALIVE
            CharacterStatus.DEAD -> STATUS_DEAD
            CharacterStatus.UNKNOWN -> STATUS_UNKNOWN
        }
        holder.gender.text = when (entity.gender) {
            CharacterGender.FEMALE -> GENDER_FEMALE
            CharacterGender.MALE -> GENDER_MALE
            CharacterGender.GENDERLESS -> GENDER_GENDERLESS
            CharacterGender.UNKNOWN -> GENDER_UNKNOWN
        }
        holder.image.load(entity.image) {
            error(R.drawable.person_placeholder)
        }
        holder.itemView.setOnClickListener {
            onItemClick.invoke(entity)
        }
        if (position == currentList.size - 1) {
            onListEnded?.invoke()
        }
    }

    companion object {

        private const val STATUS_ALIVE = "Alive"
        private const val STATUS_DEAD = "Dead"
        private const val STATUS_UNKNOWN = "Unknown"

        private const val GENDER_FEMALE = "Female"
        private const val GENDER_MALE = "Male"
        private const val GENDER_GENDERLESS = "Genderless"
        private const val GENDER_UNKNOWN = "Unknown"
    }
}
