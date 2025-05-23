package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.search.data.SearchPrefs
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.api.TrackRepository
import com.practicum.playlistmaker.sharing.domain.models.Track

class TrackInteractorImpl(private  val repository: TrackRepository,
                          private val searchPrefs: SearchPrefs) : TrackInteractor {

    override suspend fun searchTrack(request: String) : List<Track>? {

            return try {
                repository.searchTracks(request)
            }

            catch (ex: Exception) {
                null
            }

    }

    override fun getHistory(): List<Track> {
        return searchPrefs.getHistory()
    }

    override fun clearHistory() {
        searchPrefs.clearHistory()
    }

    override fun recordTrack(track: Track) {
        searchPrefs.recordTrack(track)
    }

}