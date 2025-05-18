package com.practicum.playlistmaker.createPlaylist.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.createPlaylist.ui.view_state.CreatePlaylistViewState

class CreatePlaylistViewModel: ViewModel() {

    val createPlaylistLiveData = MutableLiveData<CreatePlaylistViewState>(CreatePlaylistViewState(false))

    fun onNameChanged(playlistName: String) {
        if (playlistName.isBlank()) {
            createPlaylistLiveData.value = createPlaylistLiveData.value!!.copy(ready = false)
        }

        else {
            createPlaylistLiveData.value = createPlaylistLiveData.value!!.copy(ready = true)
        }
    }

}