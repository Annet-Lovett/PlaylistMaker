package com.practicum.playlistmaker.player.ui.view_states

import com.practicum.playlistmaker.sharing.domain.models.Track

sealed interface PlayerState {
    object Initial: PlayerState
}

data class TrackState(val progress: String,
                       val isPlaying: Boolean,
                       val track: Track
) : PlayerState



