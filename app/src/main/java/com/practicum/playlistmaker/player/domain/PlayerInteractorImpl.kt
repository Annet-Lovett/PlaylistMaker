package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.player.data.PlayerPrefs
import com.practicum.playlistmaker.sharing.domain.models.Track

class PlayerInteractorImpl(private val playerPrefs: PlayerPrefs): PlayerInteractor {

    override fun provideTrack(): Track {
        return playerPrefs.getTrack()
    }
}