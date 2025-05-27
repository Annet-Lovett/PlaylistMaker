package com.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import kotlinx.coroutines.flow.MutableStateFlow

class PlaylistMediaViewModel(): ViewModel() {

    val flow = MutableStateFlow(listOf<Playlist>())

}