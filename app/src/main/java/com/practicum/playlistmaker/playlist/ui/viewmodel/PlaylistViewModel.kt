package com.practicum.playlistmaker.playlist.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

class PlaylistViewModel(
    val playlistId: Int,
    val playlistInteractor: PlaylistInteractor, application: Application
) : AndroidViewModel(application) {

    val playlistLiveData = MutableLiveData<PlaylistViewState>(PlaylistViewState())

    init {

        updateState()

    }

    private fun createViewState(playlist: Playlist, listOfTracks: List<Track>): PlaylistViewState {

        lateinit var nameNumber: String

        when (listOfTracks.size % 10) {
            0, 5, 6, 7, 8, 9 -> nameNumber =
                getApplication<Application>().resources.getString(R.string.very_very_many_tracks)

            1 -> nameNumber = getApplication<Application>().resources.getString(R.string.track)
            2, 3, 4 -> nameNumber =
                getApplication<Application>().resources.getString(R.string.many_tracks)
        }

        return PlaylistViewState(
            cover = playlist.cover,
            nameOfThePlaylist = playlist.playlistName,
            description = playlist.playlistDescription,
            duration = durationInMinutes(listOfTracks.sumOf { it.trackTimeMillis.toLong() }),
            numberOfTheTracks = "${listOfTracks.size.toString()} $nameNumber",
            listOfTheTracks = listOfTracks
        )

    }

    private fun durationInMinutes(mills: Long): String {
        val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(mills)

        val minutesPlural = minutes.toInt() % 10

        val single = arrayOf(0, 5, 6, 7, 8, 9)

        if (single.contains(minutesPlural)) {
            return "$minutes ${getApplication<Application>().resources.getString(R.string.many_minutes)}"
        } else if (minutesPlural == 1) {
            return "$minutes ${getApplication<Application>().resources.getString(R.string.minute)}"
        } else {
            return "$minutes ${getApplication<Application>().resources.getString(R.string.a_few_minutes)}"
        }

    }

    fun selectTrack(track: Track) {
        playlistInteractor.selectTrack(track)
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = playlistInteractor.getCurrentPlaylist(playlistId)
            playlistInteractor.removeTrack(track, playlist)
            updateState()
        }
    }

    fun updateState() {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = playlistInteractor.getCurrentPlaylist(playlistId)
            val playlistTracks = playlistInteractor.getPlaylistsTracks(playlist)
            withContext(Dispatchers.Main) {
                playlistLiveData.value = createViewState(playlist, playlistTracks)
            }
        }
    }

    fun deletePlaylist() {

        viewModelScope.launch(Dispatchers.IO) {
            val playlist = playlistInteractor.getCurrentPlaylist(playlistId)
            playlistInteractor.deletePlaylist(playlist)
        }

    }

}