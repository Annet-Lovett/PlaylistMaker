package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.models.Track

interface TrackInteractor {

    fun searchTrack (request: String, consumer: TrackConsumer)

    fun getHistory (): List<Track>

    fun clearHistory()

    fun recordTrack(track: Track)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?)
    }

}