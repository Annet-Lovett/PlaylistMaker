package com.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.createPlaylist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.createPlaylist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlaylistViewModel(val playlistInteractor: PlaylistInteractor): ViewModel() {

//    val flow = playlistInteractor.getAllPlaylists()
    val flow = MutableStateFlow(listOf<Playlist>())

}