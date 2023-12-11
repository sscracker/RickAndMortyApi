package ru.example.rickandmortyproject.presentation.episodes.list

import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeEntity
import ru.example.rickandmortyproject.domain.episodes.list.model.EpisodeFilterSettings
import javax.inject.Inject

class EpisodesMatcher
    @Inject
    constructor() {
        fun isEpisodeMatches(
            filter: EpisodeFilterSettings,
            episode: EpisodeEntity,
        ): Boolean {
            val nameMatches =
                if (filter.name.isNotEmpty()) {
                    episode.name.contains(filter.name, true)
                } else {
                    true
                }

            val codeMatches =
                if (filter.code.isNotEmpty()) {
                    episode.episodeCode.contains(filter.code, true)
                } else {
                    true
                }

            return nameMatches && codeMatches
        }
    }
