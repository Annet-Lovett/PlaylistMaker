package com.practicum.playlistmaker.createPlaylist.domain.models

import com.practicum.playlistmaker.sharing.domain.models.Track

data class Playlist(
    val playlistName: String,
    val playlistId: Int,
    val playlistDescription: String,
    val cover: String? = null,
    val tracksIdList: List<Int> = emptyList(),
    val numberTracks: Int = 0,
)