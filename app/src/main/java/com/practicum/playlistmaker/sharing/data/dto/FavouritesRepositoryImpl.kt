package com.practicum.playlistmaker.sharing.data.dto

import com.practicum.playlistmaker.player.data.db.TrackFavouriteEntity
import com.practicum.playlistmaker.player.data.db.dao.TrackFavouriteDao
import com.practicum.playlistmaker.sharing.domain.api.FavouritesRepository
import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl(private val dao: TrackFavouriteDao): FavouritesRepository {

    override suspend fun addFavouriteTrack(track: Track) {

        dao.insertTrack(toEntity(track))
    }

    override suspend fun deleteTrack(track: Track) {

        dao.deleteTrack(toEntity(track))
    }

    override fun getAllFavouriteTracks(): Flow<List<Track>> {
        return dao.getTracks().map {
            it.map (::fromEntity)
        }

    }

    private fun fromEntity(entity: TrackFavouriteEntity): Track {
        return Track(
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTimeMillis = entity.trackTimeMillis.toString(),
            artworkUrl100 = entity.artworkUrl100,
            trackId = entity.trackId,
            collectionName = entity.collectionName,
            releaseDate = entity.releaseDate,
            primaryGenreName = entity.primaryGenreName,
            country = entity.country,
            previewUrl = entity.previewUrl,
            isFavourite = true

        )
    }

    private fun toEntity(track: Track): TrackFavouriteEntity {
        return TrackFavouriteEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis.toString(),
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,

        )

    }
}