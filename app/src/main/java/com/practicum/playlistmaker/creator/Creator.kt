package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.sharing.data.dto.TrackRepositoryImpl
import com.practicum.playlistmaker.sharing.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.api.TrackRepository
import com.practicum.playlistmaker.sharing.domain.impl.TrackInteractorImpl

object Creator {

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provide(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

}