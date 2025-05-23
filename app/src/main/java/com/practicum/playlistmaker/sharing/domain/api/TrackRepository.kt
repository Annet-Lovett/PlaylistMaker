package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.models.Track

interface TrackRepository {

    suspend fun searchTracks (request: String): List<Track>?

}