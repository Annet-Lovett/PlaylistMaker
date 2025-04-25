package com.practicum.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.ui.view_states.PlayerState
import com.practicum.playlistmaker.player.ui.view_states.TrackState
import com.practicum.playlistmaker.sharing.domain.api.FavouritesInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val mediaPlayer: MediaPlayer,
    private val favouritesInteractor: FavouritesInteractor,
) : ViewModel() {

    val progressFlow = MutableStateFlow<PlayerState>(PlayerState.Initial(false))

    private var progressJob: Job? = null

    init {
        preparePlayer(mediaPlayer)

        viewModelScope.launch {
            favouritesInteractor.getAllFavouriteTracks()
                .map { it.contains(playerInteractor.getTrack()) }
                .collect { isFavourite ->
                    if (progressFlow.value is TrackState) {
                        progressFlow.update { (it as TrackState).copy(isFavourite = isFavourite) }
                    }

                    else {
                        progressFlow.update {PlayerState.Initial(isFavourite)}
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
            progressFlow.update {
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
                        isFavourite = progressFlow.value.isFavourite
                    )
                }

            }
        }

        mediaPlayer.setOnCompletionListener {
            progressFlow.update {
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

        val currentState = progressFlow.value

        when (currentState) {
            is TrackState -> {
                val newState = currentState.copy(isPlaying = true)
                progressFlow.update { newState }
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

        val currentState = progressFlow.value

        when (currentState) {
            is TrackState -> {
                val newState = currentState.copy(isPlaying = false)
                progressFlow.update { newState }
            }

            else -> {}
        }

        mediaPlayer.pause()
        progressJob?.cancel()
    }

    fun toggle() {
        val trackState = progressFlow.value as? TrackState

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
        val trackState = progressFlow.value as TrackState
        progressFlow.update { trackState.copy(progress = getCurrentPosition()) }
    }

    fun onFavoriteClicked() {

        if (progressFlow.value !is TrackState) return

        val trackState = progressFlow.value as TrackState

        viewModelScope.launch {
            if (trackState.isFavourite) {
                favouritesInteractor.deleteTrack(playerInteractor.getTrack())
            } else {
                favouritesInteractor.addFavouriteTrack(playerInteractor.getTrack())
            }
        }
    }

    companion object {
        const val START_TIME = "00:00"
    }

}

