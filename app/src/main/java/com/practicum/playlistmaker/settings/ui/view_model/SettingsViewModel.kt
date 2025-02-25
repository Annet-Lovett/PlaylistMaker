package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.ui.view_states.SettingsScreenState

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private val settingsStateLiveData = MutableLiveData(SettingsScreenState(settingsInteractor.getThemeState()))

    fun getScreenStateLiveData(): LiveData<SettingsScreenState> = settingsStateLiveData

    fun changeTheme (isDark: Boolean) {
        settingsInteractor.setThemeState(isDark)
        settingsStateLiveData.value = settingsStateLiveData.value!!.copy(isDarkTheme = isDark)
        MyApplication.instance.switchTheme(isDark)
    }

}