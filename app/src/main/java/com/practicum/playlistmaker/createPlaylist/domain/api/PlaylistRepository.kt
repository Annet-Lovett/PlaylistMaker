package com.practicum.playlistmaker.createPlaylist.domain.api

import com.practicum.playlistmaker.createPlaylist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist (playlist: Playlist)

    suspend fun deletePlaylist (playlist: Playlist)

    suspend fun update (playlist: Playlist)

    fun getAllPlaylists (): Flow<List<Playlist>>



}