package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.MyApplication.Companion.KEY_FOR_THE_CURRENT_THEME_STATE
import com.practicum.playlistmaker.player.ui.PlayerActivity.Companion.KEY_FOR_CURRENT_TRACK

class SettingsPrefs {
    private val prefs: SharedPreferences = MyApplication.sharedPreferences

    fun getThemeSwitcherState(): Boolean {
        return prefs.getBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, false)
    }

    fun setThemeSwitcherState(isDark: Boolean) {
        prefs.edit { putBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, isDark) }
    }


}