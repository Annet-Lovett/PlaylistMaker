package com.practicum.playlistmaker.createPlaylist.domain.impl

import com.practicum.playlistmaker.createPlaylist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.createPlaylist.domain.api.PlaylistRepository
import com.practicum.playlistmaker.createPlaylist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl (private val repository: PlaylistRepository): PlaylistInteractor {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun update(playlist: Playlist) {
        repository.update(playlist)
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }
}