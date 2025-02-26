package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.search.data.SearchPrefs
import com.practicum.playlistmaker.sharing.data.dto.TrackRepositoryImpl
import com.practicum.playlistmaker.sharing.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.api.TrackRepository
import com.practicum.playlistmaker.sharing.domain.impl.TrackInteractorImpl

object Creator {

//    private fun getTrackRepository(): TrackRepository {
//        return TrackRepositoryImpl(RetrofitNetworkClient())
//    }
//    private fun getSearchPrefs (): SearchPrefs { return SearchPrefs(MyApplication.sharedPreferences) }
//
//    fun provideTrackInteractor(): TrackInteractor {
//        return TrackInteractorImpl(getTrackRepository(), getSearchPrefs())
//    }

}