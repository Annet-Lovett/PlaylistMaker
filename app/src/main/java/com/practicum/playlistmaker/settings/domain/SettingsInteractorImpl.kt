package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.data.SettingsPrefs

class SettingsInteractorImpl(private var settingsPrefs: SettingsPrefs): SettingsInteractor {
    override fun getThemeState(): Boolean {
        return settingsPrefs.getThemeSwitcherState()
    }

    override fun setThemeState(isDarkTheme: Boolean) {
        settingsPrefs.setThemeSwitcherState(isDarkTheme)
    }

}