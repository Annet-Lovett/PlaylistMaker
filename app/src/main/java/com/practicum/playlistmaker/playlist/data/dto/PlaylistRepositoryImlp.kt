package com.practicum.playlistmaker.playlist.data.dto

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.playlist.domain.api.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type

class PlaylistRepositoryImlp (private val dao: PlaylistDao) : PlaylistRepository {


    private val gson = Gson()
    private val type: Type = TypeToken.getParameterized(List::class.java, Integer::class.java).type

    override suspend fun addPlaylist(playlist: Playlist) {
        dao.insertPlaylist(toEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        dao.deletePlaylist(toEntity(playlist))
    }

    override suspend fun update(playlist: Playlist) {
        dao.updatePlaylists(toEntity(playlist))
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return dao.getPlaylist().map {
            it.map (::fromEntity)
        }

    }

    override suspend fun getCurrentPlaylist(plailistId: Int): Playlist {
        return fromEntity(dao.getCurrentPlaylist(plailistId))
    }

    override fun listenPlaylist(playlistId: Int): Flow<Playlist> {
        return dao.listenPlaylist(playlistId).map { fromEntity(it) }
    }

    private fun fromEntity(entity: PlaylistEntity): Playlist {

        return Playlist(
            playlistName = entity.playlistName,
            playlistId = entity.playlistId,
            playlistDescription = entity.playlistDescription,
            cover = entity.cover,
            tracksIdList = gson.fromJson(entity.tracksIdList, type),
            numberTracks = entity.numberTracks
        )
    }

    private fun toEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            cover = playlist.cover,
            tracksIdList = gson.toJson(playlist.tracksIdList, type),
            numberTracks = playlist.numberTracks
        )

    }
}