package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.dto.TrackRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.domain.impl.TrackPlayerImpl
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.api.TrackPlayer
import com.practicum.playlistmaker.sharing.domain.api.TrackRepository

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

//    fun getRepository(): TrackRepository {
//        return TrackRepositoryImpl(RetrofitNetworkClient())
//    }
//
//    fun provideTracksInteractor(): TrackInteractor {
//        return TrackInteractorImpl(getRepository())
//    }
//
//    fun provideTrackPlayer(): TrackPlayer {
//        return TrackPlayerImpl()
//    }
}




