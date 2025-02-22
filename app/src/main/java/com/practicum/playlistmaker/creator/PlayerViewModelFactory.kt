package com.practicum.playlistmaker.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.player.data.PlayerPrefs
import com.practicum.playlistmaker.player.domain.PlayerInteractorImpl
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel

class PlayerViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val playerPrefs = PlayerPrefs(MyApplication.sharedPreferences)
        return PlayerViewModel(playerInteractor = PlayerInteractorImpl(playerPrefs)) as T
    }
}