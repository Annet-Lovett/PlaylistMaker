package com.practicum.playlistmaker.playlist.ui.view_states

import com.practicum.playlistmaker.sharing.domain.models.Track

data class PlaylistViewState(
    val cover: String? = null,
    val nameOfThePlaylist: String = "",
    val year: String = "",
    val duration: String = "",
    val numberOfTheTracks: String = "",
    val listOfTheTracks: List<Track> = emptyList()
) {
}