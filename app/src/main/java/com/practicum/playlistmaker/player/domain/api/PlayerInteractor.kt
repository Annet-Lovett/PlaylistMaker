package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.sharing.domain.models.Track

interface PlayerInteractor {

    fun provideTrack () : Track

}