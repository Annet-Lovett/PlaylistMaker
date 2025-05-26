package com.practicum.playlistmaker.playlist.domain.models

data class Playlist(
    val playlistName: String,
    val playlistId: Int,
    val playlistDescription: String,
    val cover: String? = null,
    val tracksIdList: List<Int> = emptyList(),
    val numberTracks: Int = 0,
)