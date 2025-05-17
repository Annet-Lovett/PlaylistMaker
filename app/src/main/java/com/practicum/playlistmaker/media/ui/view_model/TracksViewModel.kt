package com.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.practicum.playlistmaker.sharing.domain.api.FavouritesInteractor
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.models.Track

class TracksViewModel (
    private val favouritesInteractor: FavouritesInteractor,
    private val trackInteractor: TrackInteractor,
): ViewModel() {

    val favouritesLiveData: LiveData <List<Track>> = favouritesInteractor.getAllFavouriteTracks().asLiveData()

    fun selectTrack(track: Track) {
        trackInteractor.recordTrack(track)
    }

}