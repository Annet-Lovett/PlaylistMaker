package com.practicum.playlistmaker.playlist.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.ui.view_states.PlaylistViewState
import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

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
            description = playlist.playlistDescription,
            duration = durationInMinutes(listOfTracks.sumOf { it.trackTimeMillis.toLong() }),
            numberOfTheTracks = listOfTracks.size.toString(),
            listOfTheTracks = listOfTracks
        )

    }

    private fun durationInMinutes (mills: Long): String {
        val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(mills)

        val minutesPlural = minutes.toInt() % 10

        val single = arrayOf(0, 5, 6 ,7, 8, 9)
        val few = arrayOf(2, 3, 4)

        if (single.contains(minutesPlural)) {
            return "$minutes минута"
        }

        else if (minutesPlural == 1) {
            return "$minutes минутс"
        }

        else  {
            return "$minutes минут"
        }

    }

}