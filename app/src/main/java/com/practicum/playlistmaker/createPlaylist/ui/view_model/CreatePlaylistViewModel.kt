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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistViewModel(val playlistId: Int,
                              val application: Application,
                              val playlistInteractor: PlaylistInteractor): ViewModel() {

    val createPlaylistLiveData: MutableLiveData<CreatePlaylistViewState>

    private val state: CreatePlaylistViewState get() = createPlaylistLiveData.value!!

    init {

        createPlaylistLiveData = MutableLiveData<CreatePlaylistViewState>(CreatePlaylistViewState(false))

        if (playlistId > 0) {
            viewModelScope.launch(Dispatchers.IO) {
                val playlist = playlistInteractor.getCurrentPlaylist(playlistId)
                withContext(Dispatchers.Main) {
                    createPlaylistLiveData.value = CreatePlaylistViewState(
                        ready = playlist.playlistName.isNotBlank(),
                        uri = playlist.cover?.let { Uri.parse(it) },
                        name = playlist.playlistName,
                        description = playlist.playlistDescription
                    )
                }
            }
        }

    }

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

        val filePath = File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistCovers")

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

    fun updatePlaylist() {

        var uri : Uri? = null

        if(state.uri != null) {
            uri = saveImageToPrivateStorage(state.uri!!)
        }

        viewModelScope.launch(Dispatchers.IO) {

            val playlist = playlistInteractor.getCurrentPlaylist(playlistId)

            playlistInteractor.update(
                playlist.copy(playlistName = state.name,
                    playlistDescription = state.description,
                    cover = uri?.toString())
            )
        }
    }

}