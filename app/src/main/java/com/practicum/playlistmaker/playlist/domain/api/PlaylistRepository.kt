package com.practicum.playlistmaker.playlist.domain.api

import com.practicum.playlistmaker.playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist (playlist: Playlist)

    suspend fun deletePlaylist (playlist: Playlist)

    suspend fun update (playlist: Playlist)

    fun getAllPlaylists (): Flow<List<Playlist>>

    suspend fun getCurrentPlaylist(playlistId: Int): Playlist

    fun listenPlaylist(playlistId: Int) : Flow<Playlist>


}