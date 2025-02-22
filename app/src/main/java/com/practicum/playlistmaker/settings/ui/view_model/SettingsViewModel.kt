package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.settings.data.SettingsPrefs
import com.practicum.playlistmaker.settings.ui.view_states.SettingsScreenState

class SettingsViewModel : ViewModel() {

    private val settingsPrefs: SettingsPrefs = SettingsPrefs()

    private val settingsStateLiveData = MutableLiveData(SettingsScreenState(settingsPrefs.getThemeSwitcherState()))

    fun getScreenStateLiveData(): LiveData<SettingsScreenState> = settingsStateLiveData

    fun changeTheme (isDark: Boolean) {
        settingsPrefs.setThemeSwitcherState(isDark)
        settingsStateLiveData.value = settingsStateLiveData.value!!.copy(isDarkTheme = isDark)
        MyApplication.instance.switchTheme(isDark)
    }

}