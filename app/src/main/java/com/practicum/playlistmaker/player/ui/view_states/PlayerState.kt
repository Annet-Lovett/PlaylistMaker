package com.practicum.playlistmaker.player.ui.view_states

import com.practicum.playlistmaker.sharing.domain.models.Track

sealed class PlayerState(
    open val isFavourite: Boolean,
) {
    data class Initial(override val isFavourite: Boolean,): PlayerState(isFavourite)
}

data class TrackState(val progress: String,
                      val isPlaying: Boolean,
                      val track: Track,
                      override val isFavourite: Boolean
) : PlayerState(isFavourite)



