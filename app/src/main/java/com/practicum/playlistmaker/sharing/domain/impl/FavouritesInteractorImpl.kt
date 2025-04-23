package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.FavouritesInteractor
import com.practicum.playlistmaker.sharing.domain.api.FavouritesRepository
import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouritesInteractorImpl(private val repository: FavouritesRepository): FavouritesInteractor {
    override fun getAllFavouriteTracks(): Flow<List<Track>> {
        return repository.getAllFavouriteTracks()
    }

    override suspend fun addFavouriteTrack(track: Track) {
        repository.addFavouriteTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        repository.deleteTrack(track)
    }
}