package ru.example.rickandmortyproject.presentation.locations.list.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity

object LocationsDiffCallback : DiffUtil.ItemCallback<LocationEntity>() {
    override fun areItemsTheSame(
        oldItem: LocationEntity,
        newItem: LocationEntity,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: LocationEntity,
        newItem: LocationEntity,
    ) = oldItem == newItem
}
