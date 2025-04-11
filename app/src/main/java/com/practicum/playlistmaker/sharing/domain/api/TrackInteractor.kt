package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.models.Track

interface TrackInteractor {

    suspend fun searchTrack (request: String) : List<Track>?

    fun getHistory (): List<Track>

    fun clearHistory()

    fun recordTrack(track: Track)


}