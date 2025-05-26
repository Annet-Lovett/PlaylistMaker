package com.practicum.playlistmaker.createPlaylist.ui.view_model

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.createPlaylist.ui.view_state.CreatePlaylistViewState
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistViewModel(val application: Application, val playlistInteractor: PlaylistInteractor): ViewModel() {

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

    private fun saveImageToPrivateStorage(uri: Uri): Uri {

        val filePath: File = File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistCovers")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "first_cover_${System.currentTimeMillis()}.jpg")

        val inputStream = application.contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return Uri.fromFile(file)
    }

    fun createPlaylist () {

        var uri : Uri? = null

        if(state.uri != null) {
            uri = saveImageToPrivateStorage(state.uri!!)
        }

        viewModelScope.launch {
            playlistInteractor.addPlaylist(
                Playlist(
                    playlistName = state.name,
                    playlistId = 0,
                    playlistDescription = state.description,
                    cover = uri?.toString(),
                    tracksIdList = emptyList(),
                    numberTracks = 0
                )
            )
        }
    }

}