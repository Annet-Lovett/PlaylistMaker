package com.practicum.playlistmaker.playlist.domain.impl

import com.practicum.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.api.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.api.PlaylistTracksRepository
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl (private val repository: PlaylistRepository,
                              private val tracksRepository: PlaylistTracksRepository
): PlaylistInteractor {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun getCurrentPlaylist(playlistId: Int): Playlist {
        return repository.getCurrentPlaylist(playlistId)
    }

    override suspend fun getPlaylistsTracks(playlist: Playlist): List<Track> {
        val allTracks = tracksRepository.getAllTracks()
        return allTracks.filter { playlist.tracksIdList.contains(it.trackId) }
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

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ) {
        tracksRepository.addTrack(track = track)
        repository.update(playlist.copy(
            tracksIdList = playlist.tracksIdList.toMutableList().apply { add(track.trackId) },
            numberTracks = playlist.numberTracks + 1
        ))
    }

    override fun selectTrack(track: Track) {

    }

    override suspend fun removeTrack(track: Track, playlist: Playlist) {
        repository.update(playlist.copy(
            tracksIdList = playlist.tracksIdList.toMutableList().apply { remove(track.trackId) },
            numberTracks = playlist.numberTracks - 1
        ))
    }
}