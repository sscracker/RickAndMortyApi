package ru.example.rickandmortyproject.presentation.locations.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.example.rickandmortyproject.R
import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity

class LocationsListAdapter(
    private val onItemClick: (LocationEntity) -> Unit,
    private val onListEnded: (() -> Unit)?
) : ListAdapter<LocationEntity, LocationsListAdapter.LocationsListViewHolder>(LocationsDiffCallback) {

    class LocationsListViewHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_location_name_text_view)
        val type: TextView = itemView.findViewById(R.id.item_location_type_text_view)
        val dimension: TextView = itemView.findViewById(R.id.item_location_dimension_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)
            .let {
                LocationsListViewHolder(it)
            }

    override fun onBindViewHolder(holder: LocationsListViewHolder, position: Int) {
        val locationEntity = getItem(position)
        holder.name.text = locationEntity.name
        holder.type.text = locationEntity.type
        holder.dimension.text = locationEntity.dimension
        holder.itemView.setOnClickListener {
            onItemClick.invoke(locationEntity)
        }
        if (position == currentList.size - 1) {
            onListEnded?.invoke()
        }
    }
}
