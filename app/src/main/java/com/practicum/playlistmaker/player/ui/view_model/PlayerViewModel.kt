package com.practicum.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.ui.view_states.PlayerState
import com.practicum.playlistmaker.player.ui.view_states.TrackState
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Initial)

    private var mediaPlayer = MediaPlayer()

    private var progressExecutor = Executors.newSingleThreadScheduledExecutor()
    private var executorFuture: Future<*>? = null

    init {
        preparePlayer(mediaPlayer)
    }

    fun getScreenStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        executorFuture?.cancel(true)
        progressExecutor.shutdownNow()
    }

    private fun preparePlayer(mediaPlayer: MediaPlayer) {

        val track = playerInteractor.getTrack()

        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.value = TrackState(progress = START_TIME, isPlaying = false, track = track)

        }
        mediaPlayer.setOnCompletionListener {
            executorFuture?.cancel(true)
            progressExecutor.shutdownNow()
            progressExecutor = Executors.newSingleThreadScheduledExecutor()
            playerStateLiveData.value = TrackState(progress = START_TIME, isPlaying = false, track = track)
        }
    }



    fun startPlayer() {

        val currentState = playerStateLiveData.value

        when (currentState) {
            is TrackState -> {
                val newState = currentState.copy(isPlaying = true)
                playerStateLiveData.postValue(newState)
            }
            else -> {}
        }

        mediaPlayer.start()
        executorFuture = progressExecutor.scheduleWithFixedDelay({ updatePlayerState() }, 0, 300, TimeUnit.MILLISECONDS )

    }

    fun pausePlayer() {
        val currentState = playerStateLiveData.value

        when (currentState) {
            is TrackState -> {
                val newState = currentState.copy(isPlaying = false)
                playerStateLiveData.postValue(newState)
            }
            else -> {}
        }

        mediaPlayer.pause()
        executorFuture?.cancel(true)
        progressExecutor.shutdownNow()
        progressExecutor = Executors.newSingleThreadScheduledExecutor()
    }

    fun toggle() {
        val trackState = playerStateLiveData.value as? TrackState

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
        val trackState = playerStateLiveData.value as TrackState
        playerStateLiveData.postValue(trackState.copy(progress = getCurrentPosition()))
    }

    companion object {
        const val START_TIME = "00:00"
    }

}

