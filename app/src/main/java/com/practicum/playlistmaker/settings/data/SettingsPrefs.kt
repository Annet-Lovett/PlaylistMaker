package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.practicum.playlistmaker.MyApplication.Companion.KEY_FOR_THE_CURRENT_THEME_STATE

class SettingsPrefs(private val prefs: SharedPreferences) {

    fun getThemeSwitcherState(): Boolean {
        return prefs.getBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, false)
    }

    fun setThemeSwitcherState(isDark: Boolean) {
        prefs.edit { putBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, isDark) }
    }


}