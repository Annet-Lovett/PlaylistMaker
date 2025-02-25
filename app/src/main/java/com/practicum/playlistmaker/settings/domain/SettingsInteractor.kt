package com.practicum.playlistmaker.settings.domain

interface SettingsInteractor {

    fun getThemeState(): Boolean

    fun setThemeState(isDarkTheme: Boolean)
}