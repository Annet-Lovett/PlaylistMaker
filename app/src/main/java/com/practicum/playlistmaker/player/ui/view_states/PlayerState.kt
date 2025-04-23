package com.practicum.playlistmaker.player.ui.view_states

import com.practicum.playlistmaker.sharing.domain.api.FavouritesRepository
import com.practicum.playlistmaker.sharing.domain.models.Track

sealed interface PlayerState {
    data object Initial: PlayerState
}

data class TrackState(val progress: String,
                       val isPlaying: Boolean,
                       val track: Track,
                        val isFavourite: Boolean,
) : PlayerState



