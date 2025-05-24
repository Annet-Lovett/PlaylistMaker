package com.practicum.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.createPlaylist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.createPlaylist.domain.models.Playlist
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.ui.view_states.PlayerState
import com.practicum.playlistmaker.player.ui.view_states.TrackState
import com.practicum.playlistmaker.sharing.domain.api.FavouritesInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val mediaPlayer: MediaPlayer,
    private val favouritesInteractor: FavouritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    val playerLiveData = MutableLiveData<PlayerState>(PlayerState.Initial(false))

    val playlistsLiveData = playlistInteractor.getAllPlaylists().asLiveData()

    val eventChannel = Channel<Pair<Playlist, Boolean>>()

    private var progressJob: Job? = null

    init {
        preparePlayer(mediaPlayer)

        viewModelScope.launch {

            favouritesInteractor.getAllFavouriteTracks()
                .map { it.contains(playerInteractor.getTrack()) }
                .collect { isFavourite ->

                    if (playerLiveData.value is TrackState) {
                        playerLiveData.value = (playerLiveData.value as TrackState).copy(isFavourite = isFavourite)
                    }

                    else {
                        playerLiveData.value = PlayerState.Initial(isFavourite)
                    }

                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    private fun preparePlayer(mediaPlayer: MediaPlayer) {

        val track = playerInteractor.getTrack()

        mediaPlayer.setDataSource(track.previewUrl)

        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {

            playerLiveData.value = playerLiveData.value.let {
                if (it is TrackState) {
                    it.copy(
                        progress = START_TIME,
                        isPlaying = false,
                        track = track
                    )
                } else {
                    TrackState(
                        progress = START_TIME,
                        isPlaying = false,
                        track = track,
                        isFavourite = it!!.isFavourite
                    )
                }

            }
        }

        mediaPlayer.setOnCompletionListener {

            if (playerLiveData.value !is TrackState) {
                return@setOnCompletionListener
            }

            playerLiveData.value = playerLiveData.value.let {
                (it as TrackState).copy(
                    progress = START_TIME,
                    isPlaying = false,
                    track = track
                )
            }
            progressJob?.cancel()
        }
    }

    fun startPlayer() {

        val currentState = playerLiveData.value

        when (currentState) {
            is TrackState -> {
                val newState = currentState.copy(isPlaying = true)
                playerLiveData.value = newState
            }

            else -> {}
        }

        mediaPlayer.start()

        progressJob = viewModelScope.launch {

            while (true) {
                delay(300)
                updatePlayerState()
            }
        }

    }

    fun pausePlayer() {

        val currentState = playerLiveData.value

        when (currentState) {
            is TrackState -> {
                val newState = currentState.copy(isPlaying = false)
//                progressFlow.update { newState }
                playerLiveData.value = newState
            }

            else -> {}
        }

        mediaPlayer.pause()
        progressJob?.cancel()
    }

    fun toggle() {
        val trackState = playerLiveData.value as? TrackState

        if (trackState?.isPlaying == true) {
            pausePlayer()
        } else if (trackState?.isPlaying == false) {
            startPlayer()
        }
    }

    fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayer.currentPosition)
    }

    private fun updatePlayerState() {
        val trackState = playerLiveData.value as TrackState
        playerLiveData.value = trackState.copy(progress = getCurrentPosition())
    }

    fun onFavoriteClicked() {

        if (playerLiveData.value !is TrackState) return

        val trackState = playerLiveData.value as TrackState

        viewModelScope.launch {
            if (trackState.isFavourite) {
                favouritesInteractor.deleteTrack(playerInteractor.getTrack())
            } else {
                favouritesInteractor.addFavouriteTrack(playerInteractor.getTrack())
            }
        }
    }

    fun addTrackToPlaylist (playlist: Playlist) {

        viewModelScope.launch {

            val track = playerInteractor.getTrack()
            if (playlist.tracksIdList.contains(track)) {
                eventChannel.send(playlist to false)
            }

            else {
                playlistInteractor.update(playlist
                    .copy(tracksIdList = playlist.tracksIdList.toMutableList().apply { add(track) }))
                    eventChannel.send(playlist to true)
            }

        }

    }

    companion object {
        const val START_TIME = "00:00"
    }

}

