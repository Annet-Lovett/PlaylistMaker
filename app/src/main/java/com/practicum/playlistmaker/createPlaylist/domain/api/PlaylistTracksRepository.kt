package com.practicum.playlistmaker.createPlaylist.domain.api

import com.practicum.playlistmaker.sharing.domain.models.Track

interface PlaylistTracksRepository {

    suspend fun addTrack (track: Track)

}