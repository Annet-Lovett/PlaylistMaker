package com.practicum.playlistmaker.player.domain

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.data.PlayerPrefs
import com.practicum.playlistmaker.player.data.PlayerState
import com.practicum.playlistmaker.player.data.TrackState
import com.practicum.playlistmaker.player.ui.PlayerActivity.Companion.DURATION_FORMAT
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel : ViewModel() {

    private val playerPrefs: PlayerPrefs = PlayerPrefs()

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Initial)

    private var mediaPlayer = MediaPlayer()

    init {
        preparePlayer(mediaPlayer)
    }

    fun getScreenStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    private fun preparePlayer(mediaPlayer: MediaPlayer) {
        mediaPlayer.setDataSource(playerPrefs.getTrack().previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.value = TrackState(progress = 0f, isPlaying = false, track = playerPrefs.getTrack())

        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.value = TrackState(progress = 0f, isPlaying = false, track = playerPrefs.getTrack())
        }
    }



    fun startPlayer() {
        val trackState = playerStateLiveData.value as TrackState
        mediaPlayer.start()
        playerStateLiveData.value = trackState.copy(isPlaying = true)

    }

    fun pausePlayer() {
        val trackState = playerStateLiveData.value as TrackState
        mediaPlayer.pause()
        playerStateLiveData.value = trackState.copy(isPlaying = false)
    }

    fun toggle() {
        val trackState = playerStateLiveData.value as TrackState

        if(trackState.isPlaying) {
            pausePlayer()

        }

        else {
            startPlayer()
        }
    }

    fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss",
            Locale.getDefault()).format(mediaPlayer.currentPosition)
    }
}

