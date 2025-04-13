package com.practicum.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.ui.view_states.PlayerState
import com.practicum.playlistmaker.player.ui.view_states.TrackState
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerInteractor: PlayerInteractor,
                      private val mediaPlayer: MediaPlayer) : ViewModel() {

    val progressFlow = MutableStateFlow<PlayerState>(PlayerState.Initial)

    private var progressJob : Job? = null

    init {
        preparePlayer(mediaPlayer)
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
            progressFlow.update { TrackState(progress = START_TIME, isPlaying = false, track = track) }
        }
        mediaPlayer.setOnCompletionListener {
            progressFlow.update {TrackState(progress = START_TIME, isPlaying = false, track = track)}
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

        if(trackState?.isPlaying==true) {
            pausePlayer()
        }

        else if (trackState?.isPlaying==false){
            startPlayer()
        }
    }

    fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss",
            Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    private fun updatePlayerState() {
        val trackState = progressFlow.value as TrackState
        progressFlow.update { trackState.copy(progress = getCurrentPosition()) }
    }

    companion object {
        const val START_TIME = "00:00"
    }

}

