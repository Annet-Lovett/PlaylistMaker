package com.practicum.playlistmaker.playlist.domain.api

import com.practicum.playlistmaker.sharing.domain.models.Track

interface PlaylistTracksRepository {

    suspend fun addTrack (track: Track)

    suspend fun getAllTracks (): List<Track>

    fun selectTrack (track: Track)

}