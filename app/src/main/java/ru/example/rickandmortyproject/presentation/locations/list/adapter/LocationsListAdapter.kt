package ru.example.rickandmortyproject.presentation.locations.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.example.rickandmortyproject.R

class LocationsListAdapter : Adapter<LocationsListAdapter.LocationsListViewHolder>() {

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

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: LocationsListViewHolder, position: Int) {
        holder.name.text = "Earth"
        holder.type.text = "Planet"
        holder.dimension.text = "Dimension C-122"
    }
}
