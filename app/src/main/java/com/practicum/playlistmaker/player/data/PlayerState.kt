package com.practicum.playlistmaker.player.data

import com.practicum.playlistmaker.domain.models.Track

sealed interface PlayerState {
    object Initial: PlayerState
}

data class TrackState(val progress: String,
                       val isPlaying: Boolean,
                       val track: Track) : PlayerState



