package com.practicum.playlistmaker.sharing.domain.api

interface TrackPlayer {

    fun play(trackId: String, statusObserver: StatusObserver)
    fun pause(trackId: String)
    fun seek(trackId: String, position: Float)

    fun release(trackId: String)

    interface StatusObserver {
        fun onProgress(progress: Float)
        fun onStop()
        fun onPlay()
    }
}