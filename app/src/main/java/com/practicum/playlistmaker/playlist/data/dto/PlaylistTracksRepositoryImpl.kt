package com.practicum.playlistmaker.playlist.data.dto

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.player.ui.view.PlayerFragment.Companion.KEY_FOR_CURRENT_TRACK
import com.practicum.playlistmaker.playlist.data.db.PlaylistTracksEntity
import com.practicum.playlistmaker.playlist.data.db.dao.PlaylistTracksDao
import com.practicum.playlistmaker.playlist.domain.api.PlaylistTracksRepository
import com.practicum.playlistmaker.sharing.domain.models.Track


class PlaylistTracksRepositoryImpl (private val dao: PlaylistTracksDao, private val prefs: SharedPreferences) : PlaylistTracksRepository {

    override suspend fun addTrack(track: Track) {
        dao.insertTrack(toEntity(track))
    }

    override suspend fun getAllTracks(): List<Track> {
        return dao.getAllTracks().map { fromEntity(it) }
    }

    override fun selectTrack(track: Track) {
        prefs.edit(true) { putString(KEY_FOR_CURRENT_TRACK, Gson().toJson(track)) }
    }

    private fun fromEntity(entity: PlaylistTracksEntity): Track {

        return Track(
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTimeMillis = entity.trackTimeMillis,
            artworkUrl100 = entity.artworkUrl100,
            trackId = entity.trackId,
            collectionName = entity.collectionName,
            releaseDate = entity.releaseDate,
            primaryGenreName = entity.primaryGenreName,
            country = entity.country,
            previewUrl = entity.previewUrl,
            isFavourite = false
        )
    }

    private fun toEntity(track: Track): PlaylistTracksEntity {
        return PlaylistTracksEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )

    }

}