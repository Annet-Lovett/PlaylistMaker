package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE)
        switchTheme(sharedPreferences.getBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, false))
        instance = this
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        lateinit var instance: MyApplication
        const val KEY_FOR_SETTINGS = "key_for_settings"
        const val KEY_FOR_THE_CURRENT_THEME_STATE = "KEY_FOR_THE_CURRENT_THEME_STATE"
    }

}




