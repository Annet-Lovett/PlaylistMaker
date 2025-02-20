package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TrackRepository {

    fun searchTracks (request: String): List<Track>?

}