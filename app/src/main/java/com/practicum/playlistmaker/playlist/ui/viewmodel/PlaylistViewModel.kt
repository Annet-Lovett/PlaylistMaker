package com.practicum.playlistmaker.playlist.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.ui.view_states.TrackState
import com.practicum.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.ui.view_states.PlaylistViewState
import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(playlistId: Int,
    val playlistInteractor: PlaylistInteractor): ViewModel() {

        val playlistLiveData = MutableLiveData<PlaylistViewState>(PlaylistViewState())

        init {

            viewModelScope.launch(Dispatchers.IO) {
                val playlist = playlistInteractor.getCurrentPlaylist(playlistId)
                val playlistTracks = playlistInteractor.getPlaylistsTracks(playlist)
                withContext(Dispatchers.Main) { playlistLiveData.value = createViewState(playlist, playlistTracks) }
            }

        }

    private fun createViewState(playlist: Playlist, listOfTracks: List<Track>): PlaylistViewState {

        return PlaylistViewState(
            cover = playlist.cover,
            nameOfThePlaylist = playlist.playlistName,
            year = "2025", //FIXME
            duration = "100", //FIXME
            numberOfTheTracks = listOfTracks.size.toString(),
            listOfTheTracks = listOfTracks
        )

    }

}