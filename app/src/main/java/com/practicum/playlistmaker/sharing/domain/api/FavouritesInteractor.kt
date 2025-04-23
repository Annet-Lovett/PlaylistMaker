package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {

    fun getAllFavouriteTracks(): Flow<List<Track>>

    suspend fun addFavouriteTrack(track: Track)

    suspend fun deleteTrack(track: Track)

}