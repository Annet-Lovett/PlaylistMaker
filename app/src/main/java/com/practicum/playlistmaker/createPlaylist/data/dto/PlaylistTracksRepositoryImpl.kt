package com.practicum.playlistmaker.createPlaylist.data.dto

import com.practicum.playlistmaker.createPlaylist.data.db.PlaylistTrackEntity
import com.practicum.playlistmaker.createPlaylist.data.db.dao.PlaylistTracksDao
import com.practicum.playlistmaker.createPlaylist.domain.api.PlaylistTracksRepository
import com.practicum.playlistmaker.sharing.domain.models.Track


class PlaylistTracksRepositoryImpl (private val dao: PlaylistTracksDao) : PlaylistTracksRepository {

    override suspend fun addTrack(track: Track) {
        dao.insertTrack(toEntity(track))
    }

    private fun fromEntity(entity: PlaylistTrackEntity): Track {

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

    private fun toEntity(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
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