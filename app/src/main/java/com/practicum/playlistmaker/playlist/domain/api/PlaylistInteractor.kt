package com.practicum.playlistmaker.playlist.domain.api

import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun update (playlist: Playlist)

    suspend fun addPlaylist (playlist: Playlist)

    suspend fun deletePlaylist (playlist: Playlist)

    suspend fun addTrackToPlaylist (track: Track, playlist: Playlist)


}