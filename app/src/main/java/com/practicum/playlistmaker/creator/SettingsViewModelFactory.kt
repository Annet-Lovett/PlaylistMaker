package com.practicum.playlistmaker.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.settings.data.SettingsPrefs
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val settingPrefs = SettingsPrefs(MyApplication.sharedPreferences)
        return SettingsViewModel(settingsInteractor = SettingsInteractorImpl(settingPrefs)) as T
    }
}