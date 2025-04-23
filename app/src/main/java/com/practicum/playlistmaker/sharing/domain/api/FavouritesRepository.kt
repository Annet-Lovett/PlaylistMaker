package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    suspend fun addFavouriteTrack (track: Track)
    suspend fun deleteTrack (track: Track)
    fun getAllFavouriteTracks (): Flow<List<Track>>

}