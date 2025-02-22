package com.practicum.playlistmaker.player.data

import com.practicum.playlistmaker.domain.models.Track

data class PlayerScreenState(val playerState: PlayerState)

sealed interface PlayerState {
    object Initial: PlayerState
}

data class TrackState(val progress: Float,
                       val isPlaying: Boolean,
                       val track: Track) : PlayerState



