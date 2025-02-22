package com.practicum.playlistmaker.settings.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.search.data.SearchScreenState
import com.practicum.playlistmaker.settings.data.SettingsPrefs
import com.practicum.playlistmaker.settings.data.SettingsScreenState

class SettingsViewModel : ViewModel() {

    private val settingsPrefs: SettingsPrefs = SettingsPrefs()

    private val settingsStateLiveData = MutableLiveData(SettingsScreenState(settingsPrefs.getThemeSwitcherState()))

    fun getScreenStateLiveData(): LiveData<SettingsScreenState> = settingsStateLiveData

    fun changeTheme (isDark: Boolean) {
        settingsPrefs.setThemeSwitcherState(isDark)
        settingsStateLiveData.value = settingsStateLiveData.value!!.copy(isDarkTheme = isDark)
        MyApplication.instance.switchTheme(isDark)
    }


//    sharedPreferences = getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE)
//
//    switchTheme=findViewById(R.id.theme_switcher)
//
//    switchTheme.isChecked = sharedPreferences.getBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, false)
//
//    switchTheme.setOnCheckedChangeListener { _, checked ->
//        sharedPreferences.edit()
//            .putBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, checked)
//            .apply()
//        (applicationContext as MyApplication).switchTheme(sharedPreferences.getBoolean(
//            KEY_FOR_THE_CURRENT_THEME_STATE, false))
//    }

}