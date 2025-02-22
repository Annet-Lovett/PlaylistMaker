package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.sharing.domain.models.Track

interface PlayerInteractor {

    fun getTrack () : Track

}