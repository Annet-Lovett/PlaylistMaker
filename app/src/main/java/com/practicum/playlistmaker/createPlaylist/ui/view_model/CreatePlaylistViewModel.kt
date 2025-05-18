package com.practicum.playlistmaker.createPlaylist.ui.view_model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.createPlaylist.ui.view_state.CreatePlaylistViewState

class CreatePlaylistViewModel: ViewModel() {

    val createPlaylistLiveData = MutableLiveData<CreatePlaylistViewState>(CreatePlaylistViewState(false))

    private val state: CreatePlaylistViewState get() = createPlaylistLiveData.value!!

    fun onNameChanged(playlistName: String) {

        if (playlistName.isBlank()) {
            createPlaylistLiveData.value = createPlaylistLiveData.value!!.copy(ready = false, name = playlistName)

        }

        else {
            createPlaylistLiveData.value = createPlaylistLiveData.value!!.copy(ready = true, name = playlistName)
        }
    }

    fun onDescriptionChanged(description: String) {

            createPlaylistLiveData.value = state.copy(description = description)
    }

    fun onPickImage(uri: Uri?) {

        createPlaylistLiveData.value = createPlaylistLiveData.value!!.copy(uri = uri)
    }

    fun isValueEmpty (): Boolean {
        return state.uri == null && state.description == "" && state.name == ""
    }

}