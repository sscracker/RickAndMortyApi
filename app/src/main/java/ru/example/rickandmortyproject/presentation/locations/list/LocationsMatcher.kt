package ru.example.rickandmortyproject.presentation.locations.list

import ru.example.rickandmortyproject.domain.locations.list.model.LocationEntity
import ru.example.rickandmortyproject.domain.locations.list.model.LocationFilterSettings
import javax.inject.Inject

class LocationsMatcher
    @Inject
    constructor() {
        fun isLocationMatches(
            filters: LocationFilterSettings,
            location: LocationEntity,
        ): Boolean {
            val nameMatches =
                if (filters.name.isNotEmpty()) {
                    location.name.contains(filters.name, true)
                } else {
                    true
                }
            val typeMatches =
                if (filters.type.isNotEmpty()) {
                    location.type.contains(filters.type, true)
                } else {
                    true
                }
            val dimensionMatches =
                if (filters.dimension.isNotEmpty()) {
                    location.dimension.contains(filters.dimension, true)
                } else {
                    true
                }

            return nameMatches && typeMatches && dimensionMatches
        }
    }
